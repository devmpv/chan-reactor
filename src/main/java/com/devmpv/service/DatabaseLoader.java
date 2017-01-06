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

	// private final ThreadRepository threadRepo;
	private final BoardRepository boardRepo;
	// private final MessageRepository messageRepository;

	@Autowired
	public DatabaseLoader(ThreadRepository threadRepo, BoardRepository boardRepo, MessageRepository messageRepository) {
		// this.threadRepo = threadRepo;
		this.boardRepo = boardRepo;
		// this.messageRepository = messageRepository;
	}

	@Override
	public void run(String... strings) throws Exception {
		if (!boardRepo.exists("b")) {
			this.boardRepo.save(new Board("b", "Anything"));
		}
		if (!boardRepo.exists("po")) {
			this.boardRepo.save(new Board("po", "Politics"));
		}
		if (!boardRepo.exists("g")) {
			this.boardRepo.save(new Board("g", "Girls"));
		}
	}
}