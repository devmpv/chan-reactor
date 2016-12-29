package com.devmpv.model;

import org.springframework.data.rest.core.config.Projection;

import java.util.Set;

public interface Projections {
	@Projection(name = "inlineAttachments", types = {Thread.class, Message.class})
	public interface InlineAttachments {
		Set<Attachment> getAttachments();

		String getId();

		String getText();

		String getTimestamp();

		String getTitle();

		String getUpdated();
	}
}
