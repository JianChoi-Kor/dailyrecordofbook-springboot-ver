package com.community.dailyrecordofbook.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ListUser {

    private Long userIdx;
    private String name;
    private String realName;
    private String email;
    private String type;
    private String phone;
    private String gender;
    private String birth;
    private String searchInfo;
    private String readingVolume;
    private String best;
}
