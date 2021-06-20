package com.community.dailyrecordofbook.comment.dto;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class WriteComment {
    @NotNull
    private Long boardIdx;

    @NotNull
    private Long writerIdx;

    @NotBlank
    private String cmtContent;
}
