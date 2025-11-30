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

@RequiredArgsConstructor
@Service
public class PostsServiceImpl implements PostsService {

    private final PostsRepository postsRepository;

    @Override
    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        Posts savedPosts = postsRepository.save(requestDto.toEntity());
        return savedPosts.getId();
    }

    @Override
    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = findPostsById(id);
        posts.update(requestDto.getTitle(), requestDto.getContent());
        return id;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Posts posts = findPostsById(id);
        postsRepository.delete(posts);
    }

    @Override
    @Transactional(readOnly = true)
    public PostsResponseDto findById(Long id) {
        Posts entity = findPostsById(id);
        return new PostsResponseDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findAllDesc() {
        return postsRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
    }

    private Posts findPostsById(Long id) {
        return postsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        ErrorCode.POST_NOT_FOUND,
                        "게시글을 찾을 수 없습니다. id=" + id));
    }
}

