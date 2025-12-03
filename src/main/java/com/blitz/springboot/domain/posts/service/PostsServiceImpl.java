package com.blitz.springboot.domain.posts.service;

import com.blitz.springboot.common.exception.EntityNotFoundException;
import com.blitz.springboot.common.exception.ErrorCode;
import com.blitz.springboot.common.exception.UnauthorizedException;
import com.blitz.springboot.domain.posts.Posts;
import com.blitz.springboot.domain.posts.PostsRepository;
import com.blitz.springboot.domain.posts.dto.PostsListResponseDto;
import com.blitz.springboot.domain.posts.dto.PostsResponseDto;
import com.blitz.springboot.domain.posts.dto.PostsSaveRequestDto;
import com.blitz.springboot.domain.posts.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public Long update(Long id, PostsUpdateRequestDto requestDto, String userEmail) {
        Posts posts = findPostsById(id);
        validateAuthor(posts, userEmail);
        posts.update(requestDto.getTitle(), requestDto.getContent());
        log.info("게시글 수정 완료: id={}, title={}, userEmail={}", id, requestDto.getTitle(), userEmail);
        return id;
    }

    @Override
    @Transactional
    public void delete(Long id, String userEmail) {
        Posts posts = findPostsById(id);
        validateAuthor(posts, userEmail);
        postsRepository.delete(posts);
        log.info("게시글 삭제 완료: id={}, userEmail={}", id, userEmail);
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

    /**
     * 게시글 작성자 권한 검증
     *
     * @param posts 검증할 게시글
     * @param userEmail 현재 로그인한 사용자의 이메일
     * @throws UnauthorizedException 작성자가 아닌 경우
     */
    private void validateAuthor(Posts posts, String userEmail) {
        if (userEmail == null) {
            log.warn("로그인하지 않은 사용자의 게시글 수정/삭제 시도: postId={}", posts.getId());
            throw new UnauthorizedException(
                    ErrorCode.UNAUTHORIZED_POST_ACCESS,
                    "로그인이 필요합니다."
            );
        }

        if (!posts.isAuthor(userEmail)) {
            log.warn("권한 없는 사용자의 게시글 수정/삭제 시도: postId={}, userEmail={}, author={}",
                    posts.getId(), userEmail, posts.getAuthor());
            throw new UnauthorizedException(
                    ErrorCode.UNAUTHORIZED_POST_ACCESS,
                    "게시글을 수정/삭제할 권한이 없습니다."
            );
        }
    }
}

