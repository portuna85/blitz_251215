package com.blitz.springboot.service;

import com.blitz.springboot.common.exception.EntityNotFoundException;
import com.blitz.springboot.common.exception.ErrorCode;
import com.blitz.springboot.domain.posts.Posts;
import com.blitz.springboot.domain.posts.PostsRepository;
import com.blitz.springboot.web.dto.PostsListResponseDto;
import com.blitz.springboot.web.dto.PostsResponseDto;
import com.blitz.springboot.web.dto.PostsSaveRequestDto;
import com.blitz.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 게시글 비즈니스 로직 서비스
 * SRP(Single Responsibility Principle): 게시글 비즈니스 로직만 담당
 * OCP(Open-Closed Principle): 인터페이스를 통한 확장 가능
 */
@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        Posts savedPosts = postsRepository.save(requestDto.toEntity());
        return savedPosts.getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = findPostsById(id);
        posts.update(requestDto.getTitle(), requestDto.getContent());
        return id;
    }

    @Transactional
    public void delete(Long id) {
        Posts posts = findPostsById(id);
        postsRepository.delete(posts);
    }

    @Transactional(readOnly = true)
    public PostsResponseDto findById(Long id) {
        Posts entity = findPostsById(id);
        return new PostsResponseDto(entity);
    }

    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findAllDesc() {
        return postsRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * ID로 게시글 조회 (private 헬퍼 메서드)
     * DRY(Don't Repeat Yourself) 원칙 준수
     */
    private Posts findPostsById(Long id) {
        return postsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        ErrorCode.POST_NOT_FOUND,
                        "게시글을 찾을 수 없습니다. id=" + id));
    }
}
