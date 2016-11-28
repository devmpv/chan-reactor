package com.devmpv.model;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MessageRepository extends PagingAndSortingRepository<Message, Serializable> {

	List<Message> findByThread(Thread thread, Pageable pageble);

	List<Message> findByThreadOrderByTimestamp(Thread thread);
}
