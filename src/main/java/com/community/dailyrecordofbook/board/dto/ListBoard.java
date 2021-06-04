package com.community.dailyrecordofbook.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ListBoard {

    private Long boardIdx;
    private Long categoryIdx;
    private String title;
    private String mainImage;
    private String realName;
    private String picture;
}
