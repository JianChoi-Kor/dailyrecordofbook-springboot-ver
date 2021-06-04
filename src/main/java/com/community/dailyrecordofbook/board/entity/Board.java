package com.community.dailyrecordofbook.board.entity;

import com.community.dailyrecordofbook.board.dto.Write;
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
    private Long categoryIdx;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private Integer hitCount;

    @Column
    private Long writerIdx;

    @Column
    private String useAt;

    @Column
    private String mainImage;


    public Board (Write write) {
        this.categoryIdx = write.getCategoryIdx();
        this.title = write.getTitle();
        this.content = write.getContent();
        this.hitCount = write.getHitCount();
        this.writerIdx = write.getWriterIdx();
        this.useAt = write.getUseAt();
        this.mainImage = write.getMainImage();
    }

    public Board updateBoard(Write write) {
        this.title = write.getTitle();
        this.content = write.getContent();
        this.mainImage = write.getMainImage();
        return this;
    }
}
