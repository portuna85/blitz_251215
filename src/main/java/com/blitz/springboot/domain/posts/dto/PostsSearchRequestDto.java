package com.blitz.springboot.domain.posts.dto;

public record PostsSearchRequestDto(String keyword, int page, int size) {
    public PostsSearchRequestDto(String keyword, Integer page, Integer size) {
        this(
                keyword != null ? keyword : "",
                page != null && page >= 0 ? page : 0,
                size != null && size > 0 ? size : 10
        );
    }
}

