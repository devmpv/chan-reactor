package com.devmpv.model;

import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Thread extends Message {

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "thread")
	private Set<Message> messages;

	@ManyToOne
	@JoinColumn(name = "board_id", nullable = false)
	private Board board;

	@Column(nullable = false)
	private Long updated;

	public Thread() {
		setTimestamp(System.currentTimeMillis());
		this.updated = System.currentTimeMillis();
	}

	public Thread(Board board, String title, String text) {
		this.board = board;
		setThread(null);
		setTitle(title);
		setText(text);
		setTimestamp(System.currentTimeMillis());
		this.updated = System.currentTimeMillis();
	}

	public Board getBoard() {
		return board;
	}

	public Set<Message> getMessages() {
		return messages;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public void setMessages(Set<Message> messages) {
		this.messages = messages;
	}

	public Long getUpdated() {
		return updated;
	}

	public void setUpdated(Long updated) {
		this.updated = updated;
	}
}
