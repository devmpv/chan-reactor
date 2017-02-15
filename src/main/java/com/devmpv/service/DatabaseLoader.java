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
	private final BoardRepository boardRepo;

	@Autowired
	public DatabaseLoader(ThreadRepository threadRepo, BoardRepository boardRepo, MessageRepository messageRepository) {
		this.boardRepo = boardRepo;
	}

	@Override
	public void run(String... strings) throws Exception {
		if (!boardRepo.exists("b")) {
			this.boardRepo.save(new Board("b", "Anything"));
		}
		if (!boardRepo.exists("po")) {
			this.boardRepo.save(new Board("po", "Politics"));
		}
		if (!boardRepo.exists("dev")) {
			this.boardRepo.save(new Board("dev", "Development"));
		}
	}
}