package com.community.dailyrecordofbook.board.entity;

import com.community.dailyrecordofbook.common.entity.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column
    private Integer category;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private Integer hitCount;

    @Column
    private Integer userIdx;

    @Column
    private String userName;

    @Column
    private String useAt;

    @Column
    private String mainImage;

}
