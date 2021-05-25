package com.community.dailyrecordofbook.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Write {

    private Long idx;

    private Integer category;

    private String title;

    private String content;

    private Integer hitCount;

    private Long writerIdx;

    private String useAt;

    private String mainImage;
}
