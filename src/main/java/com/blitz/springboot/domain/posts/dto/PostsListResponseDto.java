package com.blitz.springboot.domain.posts.dto;

import com.blitz.springboot.domain.posts.Posts;

import java.time.LocalDateTime;

public record PostsListResponseDto(
        Long id,
        String title,
        String author,
        String authorEmail,
        LocalDateTime modifiedDate
) {
    public PostsListResponseDto(Posts entity) {
        this(
                entity.getId(),
                entity.getTitle(),
                entity.getAuthor(),
                entity.getAuthorEmail(),
                entity.getModifiedDate()
        );
    }
}

