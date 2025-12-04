package com.blitz.springboot.domain.posts.service;

import com.blitz.springboot.domain.posts.dto.*;

import java.util.List;

public interface PostsService {

    Long save(PostsSaveRequestDto requestDto);

    Long update(Long id, PostsUpdateRequestDto requestDto, String userEmail);

    void delete(Long id, String userEmail);

    PostsResponseDto findById(Long id);

    PostsResponseDto findByIdWithViewCount(Long id);

    List<PostsListResponseDto> findAllDesc();

    PostsPageResponseDto findAllWithPaging(int page, int size);

    PostsPageResponseDto searchPosts(String keyword, int page, int size);

    List<PostsListResponseDto> findMyPosts(String userEmail);

    List<PostsListResponseDto> findPopularPosts(int limit);
}

