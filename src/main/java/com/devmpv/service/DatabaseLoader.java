package com.devmpv.service;

import com.devmpv.model.*;
import com.devmpv.model.Thread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseLoader implements CommandLineRunner {

	private final ThreadRepository threadRepo;
	private final BoardRepository boardRepo;

	@Autowired
	public DatabaseLoader(ThreadRepository threadRepo, BoardRepository boardRepo) {
		this.threadRepo = threadRepo;
		this.boardRepo = boardRepo;
	}

	@Override
	public void run(String... strings) throws Exception {
		Board b = this.boardRepo.save(new Board("b","Various"));
		Board po = this.boardRepo.save(new Board("po", "Politics"));
		for (int i=0;i<25;i++) {
			this.threadRepo.save(new Thread(b, String.format("B thread %d",i), String.format("Hello everybody. Here is my another thread N%d", i)));
		}
		for (int i=0;i<25;i++) {
			this.threadRepo.save(new Thread(po, String.format("PO thread %d",i), String.format("Hello everybody. Here is my another thread N%d", i)));
		}
	}
}