package com.community.dailyrecordofbook.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeleteInfo {

    private Long boardIdx;
    private Long sessionUserIdx;
}
