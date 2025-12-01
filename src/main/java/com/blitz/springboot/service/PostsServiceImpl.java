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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 게시글 서비스 구현체
 * SRP: 게시글 관련 비즈니스 로직 처리 책임
 * OCP: PostsService 인터페이스를 구현하여 확장에 열려있고 수정에 닫혀있음
 * DIP: PostsRepository 인터페이스에 의존
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class PostsServiceImpl implements PostsService {

    private final PostsRepository postsRepository;

    @Override
    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        Posts savedPosts = postsRepository.save(requestDto.toEntity());
        log.info("게시글 저장 완료: id={}, title={}", savedPosts.getId(), savedPosts.getTitle());
        return savedPosts.getId();
    }

    @Override
    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = findPostsById(id);
        posts.update(requestDto.getTitle(), requestDto.getContent());
        log.info("게시글 수정 완료: id={}, title={}", id, requestDto.getTitle());
        return id;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Posts posts = findPostsById(id);
        postsRepository.delete(posts);
        log.info("게시글 삭제 완료: id={}", id);
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
        List<PostsListResponseDto> result = postsRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new)
                .toList();
        log.debug("게시글 목록 조회 완료: count={}", result.size());
        return result;
    }

    /**
     * ID로 게시글 조회 (존재하지 않으면 예외 발생)
     *
     * @param id 게시글 ID
     * @return 조회된 게시글
     * @throws EntityNotFoundException 게시글이 존재하지 않을 경우
     */
    private Posts findPostsById(Long id) {
        return postsRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("게시글을 찾을 수 없음: id={}", id);
                    return new EntityNotFoundException(
                            ErrorCode.POST_NOT_FOUND,
                            "게시글을 찾을 수 없습니다. id=" + id
                    );
                });
    }
}

