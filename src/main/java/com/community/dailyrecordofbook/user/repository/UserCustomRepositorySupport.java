package com.community.dailyrecordofbook.user.repository;

import com.community.dailyrecordofbook.user.dto.FindEmail;
import com.community.dailyrecordofbook.user.dto.FindPassword;
import com.community.dailyrecordofbook.user.dto.ListUser;
import com.community.dailyrecordofbook.user.entity.User;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.regex.Pattern;

import static com.community.dailyrecordofbook.user.entity.QUser.user;

@Repository
public class UserCustomRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;

    public UserCustomRepositorySupport(JPAQueryFactory queryFactory) {
        super(User.class);
        this.queryFactory = queryFactory;
    }

    public List<User> findEmail(FindEmail findEmail) {
        return queryFactory
                .selectFrom(user)
                .where(user.realName.eq(findEmail.getRealName())
                .and(user.birth.eq(findEmail.getBirth()))
                .and(user.phone.eq(findEmail.getPhone())))
                .fetch();
    }

    public User findPassword(FindPassword findPassword) {
        return queryFactory
                .selectFrom(user)
                .where(user.email.eq(findPassword.getEmail())
                .and(user.realName.eq(findPassword.getRealName()))
                .and(user.birth.eq(findPassword.getBirth()))
                .and(user.phone.eq(findPassword.getPhone())))
                .fetchOne();
    }

    public User findByIdx(Long userIdx) {
        return queryFactory
                .selectFrom(user)
                .where(user.idx.eq(userIdx))
                .fetchOne();
    }

    public PageImpl<ListUser> getList(Pageable pageable, String search) {
        String pattern = "^[0-9]*$"; //숫자만
        String type = "";
        if(!ObjectUtils.isEmpty(search)) {
            if(Pattern.matches(pattern, search)) {
                type = "0"; // 전화번호인 경우
            } else {
                type = "1"; // 이름인 경우
            }
        }

        BooleanBuilder builder = new BooleanBuilder();
        if(type.equals("0")) {
            builder.and (user.phone.contains(search));
        }
        if(type.equals("1")) {
            builder.and (user.realName.contains(search));
        }

        JPQLQuery<ListUser> query = queryFactory
                .select(Projections.constructor(ListUser.class,
                        user.idx, user.name, user.realName, user.email, user.type, user.phone,
                        user.gender, user.birth, user.searchInfo, user.readingVolume, user.best))
                .from(user)
                .where(builder);
        query.orderBy(user.realName.asc());
        long totalCount = query.fetchCount();
        List<ListUser> results = getQuerydsl().applyPagination(pageable, query).fetch();
        return new PageImpl<>(results, pageable, totalCount);
    }
}
