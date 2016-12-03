package com.devmpv.model;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ThreadRepository extends PagingAndSortingRepository<Thread, Long> {
	List<Thread> findByBoard(BoardEnum board, Pageable pageble);
}
