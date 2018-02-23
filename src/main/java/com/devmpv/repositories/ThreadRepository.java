package com.devmpv.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import com.devmpv.model.Board;
import com.devmpv.model.Projections.InlineAttachments;
import com.devmpv.model.Thread;

/**
 * Thread repository
 * 
 * @author devmpv
 *
 */
@RepositoryRestResource(excerptProjection = InlineAttachments.class)
public interface ThreadRepository extends PagingAndSortingRepository<Thread, Long> {

    Long countByBoard(Board board);

    List<Thread> findByBoardOrderByUpdatedAtAsc(Board board);

    @RestResource(path = "findByBoardId", rel = "threads")
    Page<Thread> findByBoardIdOrderByUpdatedAtDesc(@Param("boardId") String boardId, Pageable page);
}
