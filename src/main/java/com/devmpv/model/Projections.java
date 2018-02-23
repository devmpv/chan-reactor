package com.devmpv.model;

import org.springframework.data.rest.core.config.Projection;

import java.util.Set;

public interface Projections {

    @Projection(name = "inlineAttachments", types = {Thread.class, Message.class})
    interface InlineAttachments {

        Set<Attachment> getAttachments();

        Set<Long> getReplyIds();

        long getId();

        String getText();

        long getCreatedAt();

        String getTitle();

        long getUpdated();
    }
}
