package com.community.dailyrecordofbook.user.dto;

import com.community.dailyrecordofbook.user.entity.Role;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Join {

    private String email;
    private Role role;
    private String type;
    private String password;
    private String realName;
    private String phone;
    private String birth;
    private String gender;
    private String searchInfo;
    private String readingVolume;
    private String best;
}
