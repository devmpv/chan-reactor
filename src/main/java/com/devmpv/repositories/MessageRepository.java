package com.devmpv.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import com.devmpv.model.Message;
import com.devmpv.model.Thread;

public interface MessageRepository extends PagingAndSortingRepository<Message, Long> {
	@RestResource(path = "thread", rel = "messages")
	Page<?> findByThreadOrderByIdAsc(@Param("uri") Thread thread, Pageable page);

	List<Message> findTop5ByThreadOrderByUpdatedDesc(Thread thread);
}
