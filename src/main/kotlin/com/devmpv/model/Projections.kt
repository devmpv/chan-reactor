package com.devmpv.model

import org.springframework.data.rest.core.config.Projection

interface Projections {

    @Projection(name = "inlineAttachments", types = arrayOf(Thread::class, Message::class))
    interface InlineAttachments {

        val attachments: Set<Attachment>

        val replyIds: Set<Long>

        val id: Long

        val text: String

        val createdAt: Long

        val updatedAt: Long

        val title: String
    }
}
