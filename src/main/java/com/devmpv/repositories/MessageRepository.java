package com.devmpv.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import com.devmpv.model.Message;
import com.devmpv.model.Projections.InlineAttachments;

/**
 * Message repository.
 * 
 * @author devmpv
 *
 */
@RepositoryRestResource(excerptProjection = InlineAttachments.class)
public interface MessageRepository extends PagingAndSortingRepository<Message, Long> {
    @RestResource(path = "count", rel = "messages")
    Long countByThreadId(@Param("id") Long id);

    @RestResource(path = "thread", rel = "messages")
    Page<Message> findByThreadIdOrderByIdAsc(@Param("id") Long id, Pageable page);

    @RestResource(path = "preview", rel = "messages")
    List<Message> findTop3ByThreadIdOrderByIdDesc(@Param("id") Long id);
}
