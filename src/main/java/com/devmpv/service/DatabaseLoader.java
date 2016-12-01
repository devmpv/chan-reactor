package com.devmpv.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.devmpv.model.BoardEnum;
import com.devmpv.model.Thread;
import com.devmpv.model.ThreadRepository;

@Component
public class DatabaseLoader implements CommandLineRunner {

	private final ThreadRepository repository;

	@Autowired
	public DatabaseLoader(ThreadRepository repository) {
		this.repository = repository;
	}

	@Override
	public void run(String... strings) throws Exception {
		this.repository.save(new Thread(BoardEnum.b, "thread 1", "Hello everybody. Here is my first thread"));
		this.repository.save(new Thread(BoardEnum.b, "thread 2", "Hello everybody. Here is my second thread"));
		this.repository.save(new Thread(BoardEnum.po, "thread 1", "Hello everybody. Here is my first /po thread"));
		this.repository.save(new Thread(BoardEnum.po, "thread 2", "Hello everybody. Here is my second /po thread"));
		this.repository.save(new Thread(BoardEnum.po, "thread 3", "Hello everybody. Here is my third /po thread"));
	}
}