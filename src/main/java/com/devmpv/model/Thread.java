package com.devmpv.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

@Entity
public class Thread extends Message {

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "thread")
	private Set<Message> messages;

	@Enumerated(EnumType.STRING)
	@NotNull
	private BoardEnum board;

	public Thread() {
        setTimestamp(System.currentTimeMillis());
	}

	public Thread(BoardEnum board, String title, String text) {
		this.board = board;
		setThread(null);
		setTitle(title);
		setText(text);
		setTimestamp(System.currentTimeMillis());
	}

	public BoardEnum getBoard() {
		return board;
	}

	public Set<Message> getMessages() {
		return messages;
	}

	public void setBoard(BoardEnum board) {
		this.board = board;
	}

	public void setMessages(Set<Message> messages) {
		this.messages = messages;
	}
}
