package com.devmpv.model;

import java.util.Set;

import org.springframework.data.rest.core.config.Projection;

public interface Projections {

	@Projection(name = "inlineAttachments", types = { Thread.class, Message.class })
    interface InlineAttachments {

		Set<Attachment> getAttachments();

		long getId();

		String getText();

		long getTimestamp();

		String getTitle();

		long getUpdated();
	}
}
