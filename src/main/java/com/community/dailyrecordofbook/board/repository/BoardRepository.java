package com.community.dailyrecordofbook.board.repository;

import com.community.dailyrecordofbook.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
