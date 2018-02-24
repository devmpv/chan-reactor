package com.devmpv.repositories

import com.devmpv.model.Board
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

/**
 * Board repository.
 *
 * @author devmpv
 */
@Repository
interface BoardRepository : PagingAndSortingRepository<Board, String>
