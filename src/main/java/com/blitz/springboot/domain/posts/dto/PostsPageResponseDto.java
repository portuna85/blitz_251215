package com.blitz.springboot.domain.posts.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class PostsPageResponseDto {
    private final List<PostsListResponseDto> posts;
    private final int currentPage;
    private final int totalPages;
    private final long totalElements;
    private final int size;
    private final boolean hasNext;
    private final boolean hasPrevious;

    @Builder
    public PostsPageResponseDto(List<PostsListResponseDto> posts, int currentPage,
                                 int totalPages, long totalElements, int size,
                                 boolean hasNext, boolean hasPrevious) {
        this.posts = posts;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.size = size;
        this.hasNext = hasNext;
        this.hasPrevious = hasPrevious;
    }
}

