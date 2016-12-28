package com.devmpv.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Message {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@JoinColumn
	private Thread thread;

	private String title;

	@Lob
	@Column(nullable = false)
	private String text;

	@Column(nullable = false)
	private Long timestamp;

	@Column(nullable = false)
	private Long updated;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "message_attachment")
	private Set<Attachment> attachments;

	protected Message() {
	}

	public Message(String title, String text) {
		setTitle(title);
		setText(text);
		setTimestamp(System.currentTimeMillis());
		setUpdated(System.currentTimeMillis());
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

	public Long getUpdated() {
		return updated;
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

	public void setUpdated(Long updated) {
		this.updated = updated;
	}
}
