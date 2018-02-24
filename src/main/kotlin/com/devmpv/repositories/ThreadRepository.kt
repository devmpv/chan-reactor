package com.devmpv.repositories

import com.devmpv.model.Board
import com.devmpv.model.Projections.InlineAttachments
import com.devmpv.model.Thread
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.data.rest.core.annotation.RestResource

/**
 * Thread repository
 *
 * @author devmpv
 */
@RepositoryRestResource(excerptProjection = InlineAttachments::class)
interface ThreadRepository : PagingAndSortingRepository<Thread, Long> {

    fun countByBoard(board: Board): Long?

    fun findByBoardOrderByUpdatedAtAsc(board: Board): List<Thread>

    @RestResource(path = "findByBoardId", rel = "threads")
    fun findByBoardIdOrderByUpdatedAtDesc(@Param("boardId") boardId: String, page: Pageable): Page<Thread>
}
