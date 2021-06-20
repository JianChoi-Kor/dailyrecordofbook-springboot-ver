package com.community.dailyrecordofbook.comment.repository;

import com.community.dailyrecordofbook.comment.entity.Comment;
import com.community.dailyrecordofbook.comment.repository.impl.CommentRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
    Optional<Comment> findByIdxAndBoardIdx(Long idx, Long boardIdx);
}
