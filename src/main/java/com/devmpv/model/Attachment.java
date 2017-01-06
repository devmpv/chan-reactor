package com.devmpv.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.devmpv.repositories.eventListeners.AttachmentEntityListener;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@EntityListeners(AttachmentEntityListener.class)
@Table(indexes = { @Index(columnList = "md5", unique = true) })
public class Attachment {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@JsonIgnore
	private String md5;

	private String name;

	@ManyToMany(mappedBy = "attachments")
	private Set<Message> messages = new HashSet<>();

	public Long getId() {
		return id;
	}

	public String getMd5() {
		return md5;
	}

	public Set<Message> getMessages() {
		return messages;
	}

	public String getName() {
		return name;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public void setName(String name) {
		this.name = name;
	}
}
