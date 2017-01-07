package com.devmpv.model;

import java.util.List;

public class ThreadPreview {
	private long count;
	private List<?> messages;

	public ThreadPreview(long count, List<?> messages) {
		this.count = count;
		this.messages = messages;
	}

	public long getCount() {
		return count;
	}

	public List<?> getMessages() {
		return messages;
	}
}
