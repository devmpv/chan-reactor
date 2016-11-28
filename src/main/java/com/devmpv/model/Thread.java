package com.devmpv.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

@Entity
public class Thread {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "thread")
	private Set<Message> messages;

	@Enumerated(EnumType.STRING)
	@NotNull
	private BoardEnum board;

	public Thread() {
	}

	public Thread(BoardEnum board) {
		this.board = board;
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
