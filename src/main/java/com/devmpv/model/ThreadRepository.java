package com.devmpv.model;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

@Repository
public interface ThreadRepository extends PagingAndSortingRepository<Thread, Long> {
	@RestResource(path = "board", rel = "threads")
	Page findByBoardOrderByUpdatedDesc(@Param("uri") Board board, Pageable p);
}
