package com.devmpv.repositories

import com.devmpv.model.Message
import com.devmpv.model.Projections.InlineAttachments
import com.devmpv.model.Thread
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.data.rest.core.annotation.RestResource

/**
 * Message repository.
 *
 * @author devmpv
 */
@RepositoryRestResource(excerptProjection = InlineAttachments::class)
interface MessageRepository : PagingAndSortingRepository<Message, Long> {

    @RestResource(path = "count", rel = "messages")
    fun countByThreadId(@Param("id") id: Long?): Long?

    @RestResource(path = "thread", rel = "messages")
    fun findByThreadIdOrderByIdAsc(@Param("id") id: Long?, page: Pageable): Page<Message>

    @RestResource(path = "preview", rel = "messages")
    fun findTop3ByThreadIdOrderByIdDesc(@Param("id") id: Long?): List<Message>

    @RestResource(exported = false)
    fun findByThreadAndIdIn(thread: Thread, ids: Set<Long>): Set<Message>
}
