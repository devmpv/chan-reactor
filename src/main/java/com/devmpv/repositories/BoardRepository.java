package com.devmpv.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.devmpv.model.Board;

/**
 * Board repository.
 * 
 * @author devmpv
 *
 */
@Repository
public interface BoardRepository extends PagingAndSortingRepository<Board, String> {

}
