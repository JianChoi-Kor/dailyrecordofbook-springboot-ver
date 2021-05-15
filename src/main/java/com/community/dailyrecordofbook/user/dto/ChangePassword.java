package com.community.dailyrecordofbook.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePassword {

    private String originPassword;
    private String newPassword;
    private String newPasswordRe;
}
