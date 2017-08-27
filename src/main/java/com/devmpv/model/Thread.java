package com.devmpv.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * Thread entity based on {@link Message}
 * 
 * @author devmpv
 *
 */
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

    public Set<Message> getMessages() {
	    return messages;
    }

    public void setBoard(Board board) {
	    this.board = board;
    }

    public void setMessages(Set<Message> messages) {
	    this.messages = messages;
    }
}
