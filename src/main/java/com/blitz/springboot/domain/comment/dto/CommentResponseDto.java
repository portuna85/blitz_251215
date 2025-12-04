package com.blitz.springboot.domain.comment.dto;

import com.blitz.springboot.domain.comment.Comment;

import java.time.LocalDateTime;

public record CommentResponseDto(
        Long id,
        String content,
        String author,
        String authorEmail,
        Long postId,
        LocalDateTime createdDate,
        LocalDateTime modifiedDate
) {
    public CommentResponseDto(Comment entity) {
        this(
                entity.getId(),
                entity.getContent(),
                entity.getAuthor(),
                entity.getAuthorEmail(),
                entity.getPost().getId(),
                entity.getCreatedDate(),
                entity.getModifiedDate()
        );
    }
}

