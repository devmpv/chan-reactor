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

import javax.transaction.Transactional;

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

	@Transactional
	public Long saveNew(Map<String, String[]> params, Map<String, MultipartFile> files) throws Exception {
		if (params.containsKey(THREAD)) {
			Thread thread = threadRepo.findOne(Long.valueOf(params.get(THREAD)[0]));
			Message message = new Message(params.get(TITLE)[0], params.get(TEXT)[0]);
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
		} else {
			Board board = boardRepo.findOne(params.get(BOARD)[0]);
			long count = threadRepo.countByBoard(board);
			if (count >= threadMaxCount) {
				List<Thread> threads = threadRepo.findByBoardOrderByUpdatedAsc(board);
				for (int i = 0; i < count - (threadMaxCount - 1); i++) {
					threadRepo.delete(threads.get(i));
				}
			}
			Thread thread = (Thread) saveAttachments(new Thread(board, params.get(TITLE)[0], params.get(TEXT)[0]),
					files);
			return threadRepo.save(thread).getId();
		}
	}
}
