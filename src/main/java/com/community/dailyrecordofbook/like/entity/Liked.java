package com.community.dailyrecordofbook.like.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@ToString
@Setter
@Getter
@Entity
public class Liked {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column
    private Long boardIdx;

    @Column
    private Long userIdx;

    @Column
    private Long cmtIdx;
}
