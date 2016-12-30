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
import com.devmpv.model.Thread;

@RepositoryRestResource(excerptProjection = InlineAttachments.class)
public interface MessageRepository extends PagingAndSortingRepository<Message, Long> {
	int countByThread(Thread board);

	@RestResource(path = "thread", rel = "messages")
	Page<?> findByThreadOrderByIdAsc(@Param("uri") Thread thread, Pageable page);

	List<Message> findTop5ByThreadOrderByUpdatedDesc(Thread thread);
}
