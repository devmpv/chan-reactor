package com.devmpv.service;

import static com.devmpv.config.Const.BOARD;
import static com.devmpv.config.Const.TEXT;
import static com.devmpv.config.Const.THREAD;
import static com.devmpv.config.Const.TITLE;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.safety.Whitelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.devmpv.model.Attachment;
import com.devmpv.model.Board;
import com.devmpv.model.Message;
import com.devmpv.model.Thread;
import com.devmpv.repositories.BoardRepository;
import com.devmpv.repositories.MessageRepository;
import com.devmpv.repositories.ThreadRepository;

@Service
public class MessageService {

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

	private OutputSettings textSettings = new OutputSettings();

	@PostConstruct
	private void postConstruct() {
		textSettings.prettyPrint(false);
	}

	private String prepareText(String input) {
		String cleanHtml = Jsoup.clean(input, "", Whitelist.basic(), textSettings);
		cleanHtml = cleanHtml.replaceAll("&gt;&gt;([0-9]{1,8})", "<a id='reply-link' key='$1'>$1</a>");
		return cleanHtml;
	}

	private Message saveAttachments(Message message, Map<String, MultipartFile> files) throws Exception {
		Set<Attachment> result = new HashSet<>();
		Set<String> errors = new HashSet<>();
		files.forEach((name, file) -> {
			try {
				result.add(attachmentService.add(file));
			} catch (IllegalStateException | IOException e) {
				errors.add(name);
			}
		});
		if (!errors.isEmpty()) {
			throw new Exception("Error saving files: " + errors.toString());
		}
		message.setAttachments(result);
		return message;
	}

	private Long saveMessage(Map<String, String[]> params, Map<String, MultipartFile> files, String text)
			throws Exception {
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
			throw new Exception("Thread limit exceeded!");
		}
		return messageRepo.save(message).getId();
	}

	@Transactional
	public Long saveNew(Map<String, String[]> params, Map<String, MultipartFile> files) throws Exception {
		String text = prepareText(params.get(TEXT)[0]);
		if (params.containsKey(THREAD)) {
			return saveMessage(params, files, text);
		} else {
			return saveThread(params, files, text);
		}
	}

	private Long saveThread(Map<String, String[]> params, Map<String, MultipartFile> files, String text)
			throws Exception {
		Board board = boardRepo.findOne(params.get(BOARD)[0]);
		long count = threadRepo.countByBoard(board);
		if (count >= threadMaxCount) {
			List<Thread> threads = threadRepo.findByBoardOrderByUpdatedAsc(board);
			for (int i = 0; i < count - (threadMaxCount - 1); i++) {
				threadRepo.delete(threads.get(i));
			}
		}
		Thread thread = (Thread) saveAttachments(new Thread(board, params.get(TITLE)[0], text), files);
		return threadRepo.save(thread).getId();
	}
}
