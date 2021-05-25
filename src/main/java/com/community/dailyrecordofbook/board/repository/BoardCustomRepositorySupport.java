package com.community.dailyrecordofbook.board.repository;

import com.community.dailyrecordofbook.board.entity.Board;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import static com.community.dailyrecordofbook.board.entity.QBoard.board;

@Repository
public class BoardCustomRepositorySupport extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public BoardCustomRepositorySupport(JPAQueryFactory queryFactory) {
        super(Board.class);
        this.queryFactory = queryFactory;
    }

    public Board findByIdx(Long boardIdx) {
        return queryFactory
                .selectFrom(board)
                .where(board.idx.eq(boardIdx))
                .fetchOne();
    }
}
