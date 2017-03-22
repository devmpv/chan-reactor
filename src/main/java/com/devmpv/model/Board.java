package com.devmpv.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Entity containing Information about board.
 * 
 * @author devmpv
 *
 */
@Entity
public class Board {
    @Id
    private String id;

    private String title;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "board")
    private Set<Thread> threads;

    protected Board() {
    }

    public Board(String id, String title) {
	this.id = id;
	this.title = title;
    }

    public String getId() {
	return id;
    }

    public Set<Thread> getThreads() {
	return threads;
    }

    public String getTitle() {
	return title;
    }

    public void setThreads(Set<Thread> threads) {
	this.threads = threads;
    }

    public void setTitle(String title) {
	this.title = title;
    }
}
