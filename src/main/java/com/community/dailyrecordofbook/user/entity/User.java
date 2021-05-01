package com.community.dailyrecordofbook.user.entity;

import com.community.dailyrecordofbook.common.entity.BaseTimeEntity;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String picture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private String type;

    @Column
    private String password;

    @Column
    private String realName;

    @Column
    private String phone;

    @Column
    private String gender;

    @Column
    private String birth;

    @Column
    private String SearchInfo;

    @Column
    private String readingVolume;

    @Column
    private String best;

    @Column
    private String authKey;




    @Builder
    public User(String name, String email, String picture, Role role, String type) {
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
        this.type = type;
    }

    public User update(String name, String picture) {
        this.name = name;
        this.picture = picture;

        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }

}
