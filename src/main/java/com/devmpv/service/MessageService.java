package com.devmpv.service;

import ac.simons.oembed.OembedResponse;
import ac.simons.oembed.OembedService;
import com.devmpv.exceptions.CRException;
import com.devmpv.model.Attachment;
import com.devmpv.model.Board;
import com.devmpv.model.Message;
import com.devmpv.model.Thread;
import com.devmpv.repositories.BoardRepository;
import com.devmpv.repositories.MessageRepository;
import com.devmpv.repositories.ThreadRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.safety.Whitelist;
import org.nibor.autolink.Autolink;
import org.nibor.autolink.LinkExtractor;
import org.nibor.autolink.LinkSpan;
import org.nibor.autolink.LinkType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.EntityLinks;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.devmpv.config.Const.*;
import static com.devmpv.config.WebSocketConfig.MESSAGE_PREFIX;

/**
 * Service to manage basic message operations.
 *
 * @author devmpv
 */
@Service
public class MessageService {

    private static final String REPLY_QUOTE = "&gt;&gt;(%s)";
    private static final String REPLY_STRING = String.format(REPLY_QUOTE, "[0-9]{1,8}");
    private static final String REPLY_REPLACE = "<a id='reply-link' key='$1'>$1</a>";

    private final Pattern pattern = Pattern.compile(REPLY_STRING);

    private final LinkExtractor linkExtractor = LinkExtractor.builder().linkTypes(EnumSet.of(LinkType.URL)).build();

    private final OutputSettings textSettings = new OutputSettings();

    private final ThreadRepository threadRepo;

    private final MessageRepository messageRepo;

    private final BoardRepository boardRepo;

    private final AttachmentService attachmentService;

    private final SimpMessagingTemplate template;

    private final EntityLinks entityLinks;

    private final OembedService oembedService;

    @Value("${chan.message.maxCount}")
    private int messageMaxCount;

    @Value("${chan.message.bumpLimit}")
    private int messageBumpLimit;

    @Value("${chan.thread.maxCount}")
    private int threadMaxCount;

    @Autowired
    public MessageService(ThreadRepository threadRepo,
                          MessageRepository messageRepo,
                          BoardRepository boardRepo,
                          AttachmentService attachmentService,
                          SimpMessagingTemplate template,
                          EntityLinks entityLinks,
                          OembedService oembedService) {
        this.threadRepo = threadRepo;
        this.messageRepo = messageRepo;
        this.boardRepo = boardRepo;
        this.attachmentService = attachmentService;
        this.template = template;
        this.entityLinks = entityLinks;
        this.oembedService = oembedService;
    }

    private String getPath(Message message) {
        return entityLinks.linkForSingleResource(message.getClass(), message.getId()).toUri().getPath();
    }

    private void notify(Message message) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("thread", message.getThread().getId());
        template.convertAndSend(MESSAGE_PREFIX + "/newMessage", getPath(message), headers);
    }

    @PostConstruct
    private void postConstruct() {
        textSettings.prettyPrint(false);
    }

    private Message parseMentions(Message message, Thread thread) {
        String text = message.getText();
        Matcher matcher = pattern.matcher(text);
        Set<Long> replyIds = new HashSet<>();
        while (matcher.find()) {
            replyIds.add(Long.parseLong(matcher.group(1)));
        }
        Set<Message> mentions = messageRepo.findByThreadAndIdIn(thread, replyIds);
        if (replyIds.contains(thread.getId())) {
            mentions.add(thread);
        }
        for (Message m : mentions) {
            text = text.replaceAll(String.format(REPLY_QUOTE, String.valueOf(m.getId())), REPLY_REPLACE);
        }
        message.setText(text);
        message.setReplyTo(mentions);
        return message;
    }

    private String parseText(String input) {
        String result = Jsoup.clean(input, "", Whitelist.basic(), textSettings);

        Iterable<LinkSpan> links = linkExtractor.extractLinks(result);
        result = Autolink.renderLinks(result, links, (link, text, sb) -> {
            String url = text.subSequence(link.getBeginIndex(), link.getEndIndex()).toString();
            Optional<OembedResponse> response = oembedService.getOembedResponseFor(url);
            sb.append("<a href=\"").append(url).append("\">").append(url).append("</a>");
            response.ifPresent(oembedResponse -> sb.append("<div>").append(oembedResponse.getHtml()).append("</div>"));
        });
        return result;
    }

    private Message saveAttachments(Message message, Map<String, MultipartFile> files) {
        Set<Attachment> result = new HashSet<>();
        Set<String> errors = new HashSet<>();
        files.forEach((name, file) -> {
            try {
                result.add(attachmentService.add(file));
            } catch (IllegalStateException | IOException e) {
                errors.add(name);
                throw new CRException(e.getMessage(), e);
            }
        });
        if (!errors.isEmpty()) {
            throw new CRException("Error saving files: " + errors.toString());
        }
        message.setAttachments(result);
        return message;
    }

    private Message saveMessage(Map<String, String[]> params, Map<String, MultipartFile> files, String text) {
        Thread thread = threadRepo.findById(Long.valueOf(params.get(THREAD)[0])).get();
        Message message = new Message(params.get(TITLE)[0], parseText(text));
        message.setThread(thread);
        saveAttachments(message, files);
        long count = messageRepo.countByThreadId(thread.getId());
        if (count < messageBumpLimit) {
            thread.setUpdatedAt(message.getCreatedAt());
            threadRepo.save(thread);
        }
        if (count >= messageMaxCount) {
            throw new CRException("Thread limit exceeded!");
        }
        message = messageRepo.save(parseMentions(message, thread));
        return message;
    }

    @Transactional
    public Message saveNew(Map<String, String[]> params, Map<String, MultipartFile> files) {
        Message result;
        if (params.containsKey(THREAD)) {
            result = saveMessage(params, files, params.get(TEXT)[0]);
            notify(result);
        } else {
            result = saveThread(params, files, params.get(TEXT)[0]);
        }
        return result;
    }

    private Thread saveThread(Map<String, String[]> params, Map<String, MultipartFile> files, String text) {
        String parsedText = parseText(text);
        Board board = boardRepo.findById(params.get(BOARD)[0]).get();
        long count = threadRepo.countByBoard(board);
        if (count >= threadMaxCount) {
            List<Thread> threads = threadRepo.findByBoardOrderByUpdatedAtAsc(board);
            for (int i = 0; i < count - (threadMaxCount - 1); i++) {
                threadRepo.delete(threads.get(i));
            }
        }
        Thread thread = (Thread) saveAttachments(new Thread(board, params.get(TITLE)[0], parsedText), files);
        return threadRepo.save(thread);
    }
}
