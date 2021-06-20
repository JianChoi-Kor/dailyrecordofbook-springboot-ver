package com.community.dailyrecordofbook.comment.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
public class ModifyComment {
    @NotNull
    private Long boardIdx;

    @NotNull
    private Long idx;

    @NotBlank
    private String cmtContent;

    @NotNull
    private Long loginUserIdx;
}
