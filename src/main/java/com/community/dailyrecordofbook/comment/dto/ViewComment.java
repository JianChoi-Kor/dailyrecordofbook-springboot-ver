package com.community.dailyrecordofbook.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ViewComment {
    private Long idx;
    private Long boardIdx;
    private Long writerIdx;
    private String cmtContent;
    private String useAt;

    private String writerName;
    private String writerProfile;
    private LocalDateTime regDate;
}
