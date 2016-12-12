package com.devmpv.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
public class Board {
	@Id
	private String id;

	private String title;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "board")
	private Set<Thread> threads;

	public String getTitle() {
		return title;
	}

	public Board() {
	}

	public Board(String id, String title) {
		this.id = id;
		this.title = title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Set<Thread> getThreads() {
		return threads;
	}

	public void setThreads(Set<Thread> threads) {
		this.threads = threads;
	}

	public String getId() {
		return id;
	}
}
