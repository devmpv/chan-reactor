package com.devmpv.repositories;

import com.devmpv.model.Board;
import com.devmpv.model.Thread;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

@Repository
public interface ThreadRepository extends PagingAndSortingRepository<Thread, Long> {
	@RestResource(path = "board", rel = "threads")
	Page<?> findByBoardOrderByUpdatedDesc(@Param("uri") Board board, Pageable page);
}
