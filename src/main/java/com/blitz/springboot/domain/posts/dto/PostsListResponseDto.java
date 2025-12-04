package com.blitz.springboot.domain.posts.dto;

import com.blitz.springboot.domain.posts.Posts;

import java.time.LocalDateTime;

public record PostsListResponseDto(
        Long id,
        String title,
        String author,
        String authorEmail,
        Long viewCount,
        LocalDateTime modifiedDate
) {
    public PostsListResponseDto(Posts entity) {
        this(
                entity.getId(),
                entity.getTitle(),
                entity.getAuthor(),
                entity.getAuthorEmail(),
                entity.getViewCount(),
                entity.getModifiedDate()
        );
    }
}

