package com.blitz.springboot.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentUpdateRequestDto {

    @NotBlank(message = "댓글 내용은 필수입니다")
    private String content;

    @Builder
    public CommentUpdateRequestDto(String content) {
        this.content = content;
    }
}

