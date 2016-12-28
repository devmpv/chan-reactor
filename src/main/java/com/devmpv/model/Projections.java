package com.devmpv.model;

import java.util.Set;

import org.springframework.data.rest.core.config.Projection;

public interface Projections {
	@Projection(name = "inlineAttachments", types = { Message.class, Thread.class })
	public interface InlineAttachments {
		Set<Attachment> getAttachments();

		String getId();

		String getText();

		String getTimestamp();

		String getTitle();

		String getUpdated();
	}
}
