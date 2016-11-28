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
		this.repository.save(new Thread(BoardEnum.b));
	}
}