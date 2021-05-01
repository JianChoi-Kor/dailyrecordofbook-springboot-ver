package com.community.dailyrecordofbook.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    MAIL("ROLE_MAIL", "이메일 인증 전"),
    TEMP("ROLE_TEMP", "준회원"),
    USER("ROLE_USER", "정회원"),
    ADMIN("ROLE_ADMIN", "관리자");

    private final String key;
    private final String title;
}
