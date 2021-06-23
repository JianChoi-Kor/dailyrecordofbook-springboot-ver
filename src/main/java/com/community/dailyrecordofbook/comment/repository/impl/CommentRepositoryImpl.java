package com.community.dailyrecordofbook.comment.repository.impl;

import com.community.dailyrecordofbook.comment.dto.ViewComment;
import com.community.dailyrecordofbook.comment.entity.Comment;
import com.community.dailyrecordofbook.comment.entity.QComment;
import com.community.dailyrecordofbook.user.entity.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;

public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public CommentRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    QComment comment = QComment.comment;
    QUser user = QUser.user;

    @Override
    public List<ViewComment> getCommentList(Long boardIdx) {
        return queryFactory
                .select(Projections.constructor(ViewComment.class,
                        comment.idx,
                        comment.boardIdx,
                        comment.writerIdx,
                        comment.cmtContent,
                        comment.useAt,
                        user.realName,
                        user.picture,
                        comment.createdDate,
                        comment.likeTotal))
                .from(comment)
                .leftJoin(user).on(comment.writerIdx.eq(user.idx))
                .where(comment.boardIdx.eq(boardIdx))
                .fetch();
    }
}
