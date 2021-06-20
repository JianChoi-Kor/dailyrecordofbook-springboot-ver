package com.community.dailyrecordofbook.comment.repository.impl;

import com.community.dailyrecordofbook.comment.dto.ViewComment;

import java.util.List;

public interface CommentRepositoryCustom {
    List<ViewComment> getCommentList(Long boardIdx);
}
