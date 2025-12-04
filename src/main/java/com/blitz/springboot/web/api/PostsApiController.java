package com.blitz.springboot.web.api;

import com.blitz.springboot.config.security.oauth.dto.SessionUser;
import com.blitz.springboot.config.security.resolver.LoginUser;
import com.blitz.springboot.domain.posts.service.PostsService;
import com.blitz.springboot.domain.posts.dto.PostsListResponseDto;
import com.blitz.springboot.domain.posts.dto.PostsResponseDto;
import com.blitz.springboot.domain.posts.dto.PostsSaveRequestDto;
import com.blitz.springboot.domain.posts.dto.PostsUpdateRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/posts")
public class PostsApiController {

    private final PostsService postsService;

    @PostMapping
    public ResponseEntity<Long> save(@Valid @RequestBody PostsSaveRequestDto requestDto,
                                      @LoginUser SessionUser user) {
        if (user == null) {
            log.warn("비로그인 사용자의 게시글 작성 시도");
            throw new com.blitz.springboot.common.exception.UnauthorizedException(
                    com.blitz.springboot.common.exception.ErrorCode.UNAUTHORIZED_POST_ACCESS,
                    "로그인이 필요합니다."
            );
        }

        PostsSaveRequestDto enrichedDto = PostsSaveRequestDto.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .author(user.name())
                .authorEmail(user.email())
                .build();

        log.info("게시글 생성 요청: title={}, author={}, email={}",
                enrichedDto.getTitle(), enrichedDto.getAuthor(), enrichedDto.getAuthorEmail());
        Long savedId = postsService.save(enrichedDto);
        log.info("게시글 생성 완료: id={}", savedId);
        return ResponseEntity.created(URI.create("/api/v1/posts/" + savedId))
                .body(savedId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> update(@PathVariable Long id,
                                        @Valid @RequestBody PostsUpdateRequestDto requestDto,
                                        @LoginUser SessionUser user) {
        log.info("게시글 수정 요청: id={}, title={}, userEmail={}", id, requestDto.getTitle(),
                user != null ? user.email() : "anonymous");
        Long updatedId = postsService.update(id, requestDto, user != null ? user.email() : null);
        log.info("게시글 수정 완료: id={}", updatedId);
        return ResponseEntity.ok(updatedId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @LoginUser SessionUser user) {
        log.info("게시글 삭제 요청: id={}, userEmail={}", id, user != null ? user.email() : "anonymous");
        postsService.delete(id, user != null ? user.email() : null);
        log.info("게시글 삭제 완료: id={}", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostsResponseDto> findById(@PathVariable Long id) {
        log.debug("게시글 조회 요청: id={}", id);
        PostsResponseDto response = postsService.findByIdWithViewCount(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PostsListResponseDto>> findAll() {
        log.debug("게시글 목록 조회 요청");
        List<PostsListResponseDto> response = postsService.findAllDesc();
        log.debug("게시글 목록 조회 완료: count={}", response.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/paging")
    public ResponseEntity<com.blitz.springboot.domain.posts.dto.PostsPageResponseDto> findAllWithPaging(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.debug("게시글 페이징 조회 요청: page={}, size={}", page, size);
        com.blitz.springboot.domain.posts.dto.PostsPageResponseDto response =
                postsService.findAllWithPaging(page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<com.blitz.springboot.domain.posts.dto.PostsPageResponseDto> searchPosts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.debug("게시글 검색 요청: keyword={}, page={}, size={}", keyword, page, size);
        com.blitz.springboot.domain.posts.dto.PostsPageResponseDto response =
                postsService.searchPosts(keyword, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my")
    public ResponseEntity<List<PostsListResponseDto>> findMyPosts(@LoginUser SessionUser user) {
        if (user == null) {
            log.warn("비로그인 사용자의 내 글 조회 시도");
            throw new com.blitz.springboot.common.exception.UnauthorizedException(
                    com.blitz.springboot.common.exception.ErrorCode.UNAUTHORIZED_POST_ACCESS,
                    "로그인이 필요합니다."
            );
        }
        log.debug("내 글 조회 요청: userEmail={}", user.email());
        List<PostsListResponseDto> response = postsService.findMyPosts(user.email());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/popular")
    public ResponseEntity<List<PostsListResponseDto>> findPopularPosts(
            @RequestParam(defaultValue = "10") int limit) {
        log.debug("인기 게시글 조회 요청: limit={}", limit);
        List<PostsListResponseDto> response = postsService.findPopularPosts(limit);
        return ResponseEntity.ok(response);
    }
}
