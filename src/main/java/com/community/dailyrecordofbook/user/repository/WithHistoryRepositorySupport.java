package com.community.dailyrecordofbook.user.repository;

import com.community.dailyrecordofbook.main.entity.WithHistory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import static com.community.dailyrecordofbook.main.entity.QWithHistory.withHistory;

@Repository
public class WithHistoryRepositorySupport extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public WithHistoryRepositorySupport(JPAQueryFactory queryFactory) {
        super(WithHistory.class);
        this.queryFactory = queryFactory;
    }

    public WithHistory getWithHistory() {
        return queryFactory
                .selectFrom(withHistory)
                .orderBy(withHistory.createdDate.desc())
                .fetchFirst();
    }
}
