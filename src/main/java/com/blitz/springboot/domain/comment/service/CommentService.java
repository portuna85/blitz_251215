package com.blitz.springboot.domain.comment.service;

import com.blitz.springboot.domain.comment.dto.CommentResponseDto;
import com.blitz.springboot.domain.comment.dto.CommentSaveRequestDto;
import com.blitz.springboot.domain.comment.dto.CommentUpdateRequestDto;

import java.util.List;

public interface CommentService {

    Long save(Long postId, CommentSaveRequestDto requestDto);

    Long update(Long commentId, CommentUpdateRequestDto requestDto, String userEmail);

    void delete(Long commentId, String userEmail);

    CommentResponseDto findById(Long commentId);

    List<CommentResponseDto> findByPostId(Long postId);

    List<CommentResponseDto> findMyComments(String userEmail);

    Long countByPostId(Long postId);
}

