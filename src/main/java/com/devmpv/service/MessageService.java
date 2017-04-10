package com.devmpv.service;

import static com.devmpv.config.Const.BOARD;
import static com.devmpv.config.Const.TEXT;
import static com.devmpv.config.Const.THREAD;
import static com.devmpv.config.Const.TITLE;
import static com.devmpv.config.WebSocketConfig.MESSAGE_PREFIX;

import java.io.IOException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

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
import org.springframework.web.multipart.MultipartFile;

import com.devmpv.exceptions.CRException;
import com.devmpv.model.Attachment;
import com.devmpv.model.Board;
import com.devmpv.model.Message;
import com.devmpv.model.Thread;
import com.devmpv.repositories.BoardRepository;
import com.devmpv.repositories.MessageRepository;
import com.devmpv.repositories.ThreadRepository;

import ac.simons.oembed.OembedResponse;
import ac.simons.oembed.OembedService;

/**
 * Service to manage basic message operations.
 * 
 * @author devmpv
 *
 */
@Service
public class MessageService {

    private static final String REPLY_STRING = "&gt;&gt;([0-9]{1,8})";
    private static final String REPLY_REPLACE = "<a id='reply-link' key='$1'>$1</a>";

    @Value("${chan.message.maxCount}")
    private int messageMaxCount;

    @Value("${chan.message.bumpLimit}")
    private int messageBumpLimit;

    @Value("${chan.thread.maxCount}")
    private int threadMaxCount;

    @Autowired
    private ThreadRepository threadRepo;
    @Autowired
    private MessageRepository messageRepo;
    @Autowired
    private BoardRepository boardRepo;
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private SimpMessagingTemplate template;
    @Autowired
    private EntityLinks entityLinks;
    @Autowired
    private OembedService oembedService;

    private LinkExtractor linkExtractor = LinkExtractor.builder().linkTypes(EnumSet.of(LinkType.URL)).build();

    private OutputSettings textSettings = new OutputSettings();

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

    private String prepareText(String input) {
	String result = Jsoup.clean(input, "", Whitelist.basic(), textSettings).replaceAll(REPLY_STRING, REPLY_REPLACE);
	Iterable<LinkSpan> links = linkExtractor.extractLinks(result);
	result = Autolink.renderLinks(result, links, (link, text, sb) -> {
	    String url = text.subSequence(link.getBeginIndex(), link.getEndIndex()).toString();
	    Optional<OembedResponse> response = oembedService.getOembedResponseFor(url);
	    sb.append("<a href=\"").append(url).append("\">").append(url).append("</a>");
	    if (response.isPresent()) {
		sb.append("<div>").append(response.get().getHtml()).append("</div>");
	    }
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
	Thread thread = threadRepo.findOne(Long.valueOf(params.get(THREAD)[0]));
	Message message = new Message(params.get(TITLE)[0], text);
	message.setThread(thread);
	saveAttachments(message, files);
	long count = messageRepo.countByThreadId(thread.getId());
	if (count < messageBumpLimit) {
	    thread.setUpdated(message.getTimestamp());
	    threadRepo.save(thread);
	}
	if (count >= messageMaxCount) {
	    throw new CRException("Thread limit exceeded!");
	}
	return messageRepo.save(message);
    }

    @Transactional
    public Message saveNew(Map<String, String[]> params, Map<String, MultipartFile> files) {
	String text = prepareText(params.get(TEXT)[0]);
	Message result;
	if (params.containsKey(THREAD)) {
	    result = saveMessage(params, files, text);
	    notify(result);
	} else {
	    result = saveThread(params, files, text);
	}
	return result;
    }

    private Thread saveThread(Map<String, String[]> params, Map<String, MultipartFile> files, String text) {
	Board board = boardRepo.findOne(params.get(BOARD)[0]);
	long count = threadRepo.countByBoard(board);
	if (count >= threadMaxCount) {
	    List<Thread> threads = threadRepo.findByBoardOrderByUpdatedAsc(board);
	    for (int i = 0; i < count - (threadMaxCount - 1); i++) {
		threadRepo.delete(threads.get(i));
	    }
	}
	Thread thread = (Thread) saveAttachments(new Thread(board, params.get(TITLE)[0], text), files);
	return threadRepo.save(thread);
    }
}
