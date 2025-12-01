package com.blitz.springboot.domain.posts.service;

import com.blitz.springboot.domain.posts.dto.PostsListResponseDto;
import com.blitz.springboot.domain.posts.dto.PostsResponseDto;
import com.blitz.springboot.domain.posts.dto.PostsSaveRequestDto;
import com.blitz.springboot.domain.posts.dto.PostsUpdateRequestDto;

import java.util.List;

public interface PostsService {

    Long save(PostsSaveRequestDto requestDto);

    Long update(Long id, PostsUpdateRequestDto requestDto);

    void delete(Long id);

    PostsResponseDto findById(Long id);

    List<PostsListResponseDto> findAllDesc();
}

