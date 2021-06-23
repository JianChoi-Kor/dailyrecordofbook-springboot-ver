package com.community.dailyrecordofbook.like.repository.impl;

import com.community.dailyrecordofbook.like.entity.QLiked;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class LikeRepositoryImpl implements LikeRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public LikeRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    QLiked liked = QLiked.liked;

    @Override
    public Long getCount(Long boardIdx, Long cmtIdx) {
        return queryFactory
                .selectFrom(liked)
                .where(liked.boardIdx.eq(boardIdx).and(liked.cmtIdx.eq(cmtIdx)))
                .fetchCount();
    }
}
