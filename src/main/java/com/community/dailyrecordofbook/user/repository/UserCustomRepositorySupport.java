package com.community.dailyrecordofbook.user.repository;

import com.community.dailyrecordofbook.user.dto.FindEmail;
import com.community.dailyrecordofbook.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import static com.community.dailyrecordofbook.user.entity.QUser.user;

@Repository
public class UserCustomRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;

    public UserCustomRepositorySupport(JPAQueryFactory queryFactory) {
        super(User.class);
        this.queryFactory = queryFactory;
    }

    public User findEmail(FindEmail findEmail) {

        return queryFactory
                .selectFrom(user)
                .where(user.realName.eq(findEmail.getRealName())
                .and(user.birth.eq(findEmail.getBirth()))
                .and(user.phone.eq(findEmail.getPhone())))
                .fetchOne();
    }
}
