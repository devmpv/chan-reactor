package com.devmpv.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.devmpv.model.Board;
import com.devmpv.repositories.BoardRepository;
import com.devmpv.repositories.MessageRepository;
import com.devmpv.repositories.ThreadRepository;

@Component
public class DatabaseLoader implements CommandLineRunner {

	private final ThreadRepository threadRepo;
	private final BoardRepository boardRepo;
	private final MessageRepository messageRepository;

	@Autowired
	public DatabaseLoader(ThreadRepository threadRepo, BoardRepository boardRepo, MessageRepository messageRepository) {
		this.threadRepo = threadRepo;
		this.boardRepo = boardRepo;
		this.messageRepository = messageRepository;
	}

	@Override
	public void run(String... strings) throws Exception {
		if (!boardRepo.exists("b")) {
			Board b = this.boardRepo.save(new Board("b", "Anything"));
		}
		if (!boardRepo.exists("po")) {
			Board po = this.boardRepo.save(new Board("po", "Politics"));
		}
		if (!boardRepo.exists("g")) {
			Board po = this.boardRepo.save(new Board("g", "Girls"));
		}
		/*
		 * for (int i = 0; i < 25; i++) { Thread thread =
		 * this.threadRepo.save(new Thread(b, format("B thread %d", i),
		 * format("Hello everybody. Here is my another b thread N%d", i))); for
		 * (int j = 0; j < 27; j++) { Message message = new
		 * Message(format("Message %d in thread %d", j, i), "blablaa");
		 * message.setThread(thread); this.messageRepository.save(message);
		 * 
		 * } } for (int i = 0; i < 25; i++) { Thread thread =
		 * this.threadRepo.save(new Thread(po, format("PO thread %d", i),
		 * format("Hello everybody. Here is my another po thread N%d", i))); for
		 * (int j = 0; j < 27; j++) { Message message = new
		 * Message(format("Message %d in thread %d", j, i), "blablaa");
		 * message.setThread(thread); this.messageRepository.save(message); } }
		 */
	}
}