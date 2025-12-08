package com.blitz.springboot.domain.posts.dto;

import lombok.Builder;

import java.util.List;

public record PostsPageResponseDto(List<PostsListResponseDto> posts, int currentPage, int totalPages,
                                   long totalElements, int size, boolean hasNext, boolean hasPrevious) {
    @Builder
    public PostsPageResponseDto {
    }
}

