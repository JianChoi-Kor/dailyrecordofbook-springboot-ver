package com.community.dailyrecordofbook.comment.controller;

import com.community.dailyrecordofbook.comment.dto.ModifyComment;
import com.community.dailyrecordofbook.comment.dto.ViewComment;
import com.community.dailyrecordofbook.comment.dto.WriteComment;
import com.community.dailyrecordofbook.comment.entity.Comment;
import com.community.dailyrecordofbook.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RequestMapping("/cmt")
@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public int writeComment(@RequestBody @Valid WriteComment writeComment, Errors errors) {
        return commentService.writeComment(writeComment, errors);
    }

    @GetMapping
    public List<ViewComment> getCommentList(Long boardIdx) {
        return commentService.getCommentList(boardIdx);
    }

    @DeleteMapping
    public int deleteComment(@RequestParam Long boardIdx, @RequestParam Long idx, @RequestParam Long loginUserIdx) {
        return commentService.deleteComment(boardIdx, idx, loginUserIdx);
    }

    @PutMapping
    public int modifyComment(@RequestBody @Valid ModifyComment modifyComment, Errors errors) {
        return commentService.modifyComment(modifyComment, errors);
    }

}
