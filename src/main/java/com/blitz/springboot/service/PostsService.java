package com.blitz.springboot.service;

import com.blitz.springboot.web.dto.PostsListResponseDto;
import com.blitz.springboot.web.dto.PostsResponseDto;
import com.blitz.springboot.web.dto.PostsSaveRequestDto;
import com.blitz.springboot.web.dto.PostsUpdateRequestDto;

import java.util.List;

public interface PostsService {

    Long save(PostsSaveRequestDto requestDto);

    Long update(Long id, PostsUpdateRequestDto requestDto);

    void delete(Long id);

    PostsResponseDto findById(Long id);

    List<PostsListResponseDto> findAllDesc();
}
