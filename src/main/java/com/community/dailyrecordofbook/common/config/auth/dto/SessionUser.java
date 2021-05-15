package com.community.dailyrecordofbook.common.config.auth.dto;

import com.community.dailyrecordofbook.user.entity.Role;
import com.community.dailyrecordofbook.user.entity.User;
import lombok.Getter;

import java.io.Serializable;

/**
 * 세션에 저장하려면 직렬화를 해야 하는데
 * User 엔티티는 추후 변경사항이 있을 수 있기 때문에
 * 직렬화를 하기 위한 별도의 SessionUser 클래스 생성
 */

@Getter
public class SessionUser implements Serializable {

    private Long idx;
    private String name;
    private String email;
    private String picture;
    private Role role;
    private String type;

    public SessionUser(User user) {

        if(user.getIdx() != null) {
            this.idx = user.getIdx();
        }
        if(user.getRealName() != null) {
            this.name = user.getRealName();
        } else {
            this.name = user.getName();
        }
        this.email = user.getEmail();
        this.picture = user.getPicture();
        this.role = user.getRole();
        this.type = user.getType();
    }
}
