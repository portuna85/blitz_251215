package com.blitz.springboot.domain.posts.dto;

import com.blitz.springboot.domain.posts.Posts;

public record PostsResponseDto(
        Long id,
        String title,
        String content,
        String author
) {
    public PostsResponseDto(Posts entity) {
        this(
                entity.getId(),
                entity.getTitle(),
                entity.getContent(),
                entity.getAuthor()
        );
    }
}

