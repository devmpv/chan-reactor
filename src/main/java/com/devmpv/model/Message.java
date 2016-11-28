package com.devmpv.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Message {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "thread_id", nullable = false)
	private Thread thread;

	private String title;

	@Lob
	@Column(nullable = false)
	private String text;

	@Column(nullable = false)
	private Long timestamp;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "message_attachment")
	private Set<Attachment> attachments = new HashSet<>();

	protected Message() {
	}

	public Message(Thread thread, String title, String text) {
		this.title = title;
		this.text = text;
		this.thread = thread;
	}

	public Set<Attachment> getAttachments() {
		return attachments;
	}

	public Long getId() {
		return id;
	}

	public String getText() {
		return text;
	}

	public Thread getThread() {
		return thread;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public String getTitle() {
		return title;
	}

	public void setAttachments(Set<Attachment> attachments) {
		this.attachments = attachments;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setThread(Thread thread) {
		this.thread = thread;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
