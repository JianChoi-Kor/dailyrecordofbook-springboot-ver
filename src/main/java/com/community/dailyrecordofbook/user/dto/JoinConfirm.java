package com.community.dailyrecordofbook.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinConfirm {

    private String email;
    private String authKey;
}
