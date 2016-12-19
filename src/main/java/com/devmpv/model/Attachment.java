package com.devmpv.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(indexes = {@Index(columnList = "md5", unique = true)})
public class Attachment {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String md5;

	@ManyToMany(mappedBy = "attachments")
	private Set<Message> messages = new HashSet<>();

	public Long getId() {
		return id;
	}

	public String getMd5() {
		return md5;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}
}
