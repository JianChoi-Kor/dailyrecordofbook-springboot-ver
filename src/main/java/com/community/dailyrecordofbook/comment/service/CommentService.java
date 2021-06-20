package com.community.dailyrecordofbook.comment.service;

import com.community.dailyrecordofbook.comment.dto.ModifyComment;
import com.community.dailyrecordofbook.comment.dto.ViewComment;
import com.community.dailyrecordofbook.comment.dto.WriteComment;
import com.community.dailyrecordofbook.comment.entity.Comment;
import com.community.dailyrecordofbook.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public int writeComment(WriteComment writeComment, Errors errors) {
        if(errors.hasErrors()) {
            return 2;
        }

        try {
            commentRepository.save(new Comment(writeComment));
            return 0;
        } catch (Exception e) {
            return 1;
        }
    }

    public List<ViewComment> getCommentList(Long boardIdx) {
        return commentRepository.getCommentList(boardIdx);
    }

    public int deleteComment(Long boardIdx, Long idx, Long loginUserIdx) {
        Comment comment = commentRepository.findByIdxAndBoardIdx(idx, boardIdx).orElse(null);
        if(comment == null) {
            return 1;
        }
        if(comment.getWriterIdx() != loginUserIdx) {
            return 1;
        }
        comment.setUseAt("1");
        commentRepository.save(comment);
        return 0;
    }

    public int modifyComment(ModifyComment modifyComment, Errors errors) {
        if(errors.hasErrors()) {
            return 1; // Valid Error
        }
        Comment comment = commentRepository.findByIdxAndBoardIdx(modifyComment.getIdx(), modifyComment.getBoardIdx()).orElse(null);
        if(comment.getWriterIdx() != modifyComment.getLoginUserIdx()) {
            return 1; // 작성자와 삭제 요청한 유저가 다른 경우
        }
        comment.upDateComment(modifyComment);
        commentRepository.save(comment);
        return 0;
    }
}
