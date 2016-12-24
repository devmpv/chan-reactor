package com.devmpv.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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
	private Set<Attachment> attachments = new HashSet<>();

	protected Message() {
	}

	public Message(String title, String text) {
		setTitle(title);
		setText(text);
		setTimestamp(System.currentTimeMillis());
		setUpdated(System.currentTimeMillis());
	}

	public Long getUpdated() {
		return updated;
	}

	public void setUpdated(Long updated) {
		this.updated = updated;
	}

	public Set<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(Set<Attachment> attachments) {
		this.attachments = attachments;
	}

	public Long getId() {
		return id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Thread getThread() {
		return thread;
	}

	public void setThread(Thread thread) {
		this.thread = thread;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
