package com.community.dailyrecordofbook.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FindPassword {

    private String email;
    private String realName;
    private String birth;
    private String phone;
}
