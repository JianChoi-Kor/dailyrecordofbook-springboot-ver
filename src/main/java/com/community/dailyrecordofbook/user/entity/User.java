package com.community.dailyrecordofbook.user.entity;

import com.community.dailyrecordofbook.common.entity.BaseTimeEntity;
import com.community.dailyrecordofbook.user.dto.Join;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;

@ToString
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

    @Column(unique = true)
    private String email;

    @Column
    private String picture;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Setter
    @Column(nullable = false)
    private String type;

    @Column
    private String password;

    @Column
    private String realName;

    @Column
    private String phone;

    @Column
    private String birth;

    @Column
    private String gender;

    @Column
    private String searchInfo;

    @Column
    private String readingVolume;

    @Column
    private String best;

    @Column
    private String authKey;


    // session 저장용 생성자
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


    // join 저장용 생성자

    public User(Join join) {
        this.email = join.getEmail();
        this.role = join.getRole();
        this.type = join.getType();
        this.password = join.getPassword();
        this.realName = join.getRealName();
        this.phone = join.getPhone();
        this.birth = join.getBirth();
        this.gender = join.getGender();
        this.searchInfo = join.getSearchInfo();
        this.readingVolume = join.getReadingVolume();
        this.best = join.getBest();
    }

}
