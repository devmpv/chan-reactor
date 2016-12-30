package com.devmpv.repositories;

import com.devmpv.model.Board;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends PagingAndSortingRepository<Board, String> {

}