package com.community.dailyrecordofbook.board.repository;

import com.community.dailyrecordofbook.board.dto.ListBoard;
import com.community.dailyrecordofbook.board.entity.Board;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.community.dailyrecordofbook.board.entity.QBoard.board;
import static com.community.dailyrecordofbook.user.entity.QUser.user;

@Repository
public class BoardCustomRepositorySupport extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public BoardCustomRepositorySupport(JPAQueryFactory queryFactory) {
        super(Board.class);
        this.queryFactory = queryFactory;
    }

    public Board findByIdx (Long boardIdx) {
        return queryFactory
                .selectFrom(board)
                .where(board.idx.eq(boardIdx))
                .fetchOne();
    }

    public PageImpl<ListBoard> getList(Long categoryIdx, Pageable pageable) {
        JPQLQuery<ListBoard> query = queryFactory
                .select(Projections.constructor(ListBoard.class,
                        board.idx, board.categoryIdx, board.title, board.mainImage, user.realName, user.picture, board.createdDate))
                .from(board)
                .join(user).on(board.writerIdx.eq(user.idx))
                .where(board.categoryIdx.eq(categoryIdx).and(board.useAt.eq("0")));
        query.orderBy(board.createdDate.desc());
        long totalCount = query.fetchCount();
        List<ListBoard> results = getQuerydsl().applyPagination(pageable, query).fetch();
        return new PageImpl<>(results, pageable, totalCount);
    }

    public List<ListBoard> getCommunityList() {
        return queryFactory
                .select(Projections.constructor(ListBoard.class,
                        board.idx, board.categoryIdx, board.title, board.mainImage, user.realName, user.picture, board.createdDate))
                .from(board)
                .join(user).on(board.writerIdx.eq(user.idx))
                .where(board.categoryIdx.eq(11L).or(board.categoryIdx.eq(12L)).and(board.useAt.eq("0")))
                .orderBy(board.createdDate.desc())
                .limit(4L)
                .fetch();
    }
}
