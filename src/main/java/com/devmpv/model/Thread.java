package com.devmpv.model;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Thread extends Message {

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "thread")
	private Set<Message> messages;

	@ManyToOne
	@JoinColumn(name = "board_id", nullable = false)
	private Board board;

	public Thread() {
		setTimestamp(System.currentTimeMillis());
	}

	public Thread(Board board, String title, String text) {
		super(title, text);
		this.board = board;
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public Set<Message> getMessages() {
		return messages;
	}

	public void setMessages(Set<Message> messages) {
		this.messages = messages;
	}
}
