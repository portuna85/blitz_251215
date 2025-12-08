package com.blitz.springboot.domain.posts.service;

import com.blitz.springboot.common.exception.EntityNotFoundException;
import com.blitz.springboot.common.exception.ErrorCode;
import com.blitz.springboot.common.exception.UnauthorizedException;
import com.blitz.springboot.domain.posts.Posts;
import com.blitz.springboot.domain.posts.PostsRepository;
import com.blitz.springboot.domain.posts.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        requestDto.applyUpdate(posts);
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
    @Transactional
    public PostsResponseDto findByIdWithViewCount(Long id) {
        Posts entity = findPostsById(id);
        entity.incrementViewCount();
        log.debug("게시글 조회수 증가: id={}, viewCount={}", id, entity.getViewCount());
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

    @Override
    @Transactional(readOnly = true)
    public PostsPageResponseDto findAllWithPaging(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Posts> postsPage = postsRepository.findAll(pageable);

        List<PostsListResponseDto> posts = postsPage.getContent().stream()
                .map(PostsListResponseDto::new)
                .toList();

        log.debug("게시글 페이징 조회 완료: page={}, size={}, totalElements={}",
                page, size, postsPage.getTotalElements());

        return PostsPageResponseDto.builder()
                .posts(posts)
                .currentPage(postsPage.getNumber())
                .totalPages(postsPage.getTotalPages())
                .totalElements(postsPage.getTotalElements())
                .size(postsPage.getSize())
                .hasNext(postsPage.hasNext())
                .hasPrevious(postsPage.hasPrevious())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PostsPageResponseDto searchPosts(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Posts> postsPage = postsRepository.searchByKeyword(keyword, pageable);

        List<PostsListResponseDto> posts = postsPage.getContent().stream()
                .map(PostsListResponseDto::new)
                .toList();

        log.debug("게시글 검색 완료: keyword={}, page={}, size={}, totalElements={}",
                keyword, page, size, postsPage.getTotalElements());

        return PostsPageResponseDto.builder()
                .posts(posts)
                .currentPage(postsPage.getNumber())
                .totalPages(postsPage.getTotalPages())
                .totalElements(postsPage.getTotalElements())
                .size(postsPage.getSize())
                .hasNext(postsPage.hasNext())
                .hasPrevious(postsPage.hasPrevious())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findMyPosts(String userEmail) {
        if (userEmail == null || userEmail.isBlank()) {
            log.warn("내 글 조회 실패: 이메일 없음");
            throw new IllegalArgumentException("이메일은 필수입니다");
        }

        List<PostsListResponseDto> result = postsRepository.findByAuthorEmail(userEmail).stream()
                .map(PostsListResponseDto::new)
                .toList();

        log.debug("내 글 조회 완료: userEmail={}, count={}", userEmail, result.size());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findPopularPosts(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<PostsListResponseDto> result = postsRepository.findTopByViewCount(pageable).stream()
                .map(PostsListResponseDto::new)
                .toList();

        log.debug("인기 게시글 조회 완료: limit={}, count={}", limit, result.size());
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
