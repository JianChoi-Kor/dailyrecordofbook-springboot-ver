package com.community.dailyrecordofbook.comment.entity;

import com.community.dailyrecordofbook.comment.dto.ModifyComment;
import com.community.dailyrecordofbook.comment.dto.WriteComment;
import com.community.dailyrecordofbook.common.entity.BaseTimeEntity;
import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column
    private Long boardIdx;

    @Column
    private Long writerIdx;

    @Column
    private String cmtContent;

    @Column
    @Setter
    private String useAt;

    @Column
    @Setter
    private Long likeTotal = 0L;

    public Comment(WriteComment writeComment) {
        this.boardIdx = writeComment.getBoardIdx();
        this.writerIdx = writeComment.getWriterIdx();
        this.cmtContent = writeComment.getCmtContent();
        this.useAt = "0";
    }

    public Comment upDateComment(ModifyComment modifyComment) {
        this.cmtContent = modifyComment.getCmtContent();
        return this;
    }
}
