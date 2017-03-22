package com.devmpv.model;

import java.util.List;

/**
 * POJO fom thread preview
 * 
 * @author devmpv
 *
 */
public class ThreadPreview {

    private long count;

    private List<Message> messages;

    public ThreadPreview(long count, List<Message> messages) {
	this.count = count;
	this.messages = messages;
    }

    public long getCount() {
	return count;
    }

    public List<Message> getMessages() {
	return messages;
    }
}
