package com.blitz.springboot.web.api;

import com.blitz.springboot.config.security.oauth.dto.SessionUser;
import com.blitz.springboot.config.security.resolver.LoginUser;
import com.blitz.springboot.domain.comment.dto.CommentResponseDto;
import com.blitz.springboot.domain.comment.dto.CommentSaveRequestDto;
import com.blitz.springboot.domain.comment.dto.CommentUpdateRequestDto;
import com.blitz.springboot.domain.comment.service.CommentService;
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
@RequestMapping("/api/v1/posts/{postId}/comments")
public class CommentApiController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Long> save(@PathVariable Long postId,
                                      @Valid @RequestBody CommentSaveRequestDto requestDto,
                                      @LoginUser SessionUser user) {
        if (user == null) {
            log.warn("비로그인 사용자의 댓글 작성 시도");
            throw new com.blitz.springboot.common.exception.UnauthorizedException(
                    com.blitz.springboot.common.exception.ErrorCode.UNAUTHORIZED_POST_ACCESS,
                    "로그인이 필요합니다."
            );
        }

        CommentSaveRequestDto enrichedDto = CommentSaveRequestDto.builder()
                .content(requestDto.getContent())
                .author(user.name())
                .authorEmail(user.email())
                .build();

        log.info("댓글 생성 요청: postId={}, author={}", postId, user.name());
        Long savedId = commentService.save(postId, enrichedDto);
        return ResponseEntity.created(URI.create("/api/v1/posts/" + postId + "/comments/" + savedId))
                .body(savedId);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Long> update(@PathVariable Long postId,
                                        @PathVariable Long commentId,
                                        @Valid @RequestBody CommentUpdateRequestDto requestDto,
                                        @LoginUser SessionUser user) {
        log.info("댓글 수정 요청: commentId={}, userEmail={}", commentId,
                user != null ? user.email() : "anonymous");
        Long updatedId = commentService.update(commentId, requestDto,
                user != null ? user.email() : null);
        return ResponseEntity.ok(updatedId);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> delete(@PathVariable Long postId,
                                        @PathVariable Long commentId,
                                        @LoginUser SessionUser user) {
        log.info("댓글 삭제 요청: commentId={}, userEmail={}", commentId,
                user != null ? user.email() : "anonymous");
        commentService.delete(commentId, user != null ? user.email() : null);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> findByPostId(@PathVariable Long postId) {
        log.debug("게시글 댓글 조회 요청: postId={}", postId);
        List<CommentResponseDto> comments = commentService.findByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> findById(@PathVariable Long postId,
                                                         @PathVariable Long commentId) {
        log.debug("댓글 조회 요청: commentId={}", commentId);
        CommentResponseDto comment = commentService.findById(commentId);
        return ResponseEntity.ok(comment);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countByPostId(@PathVariable Long postId) {
        Long count = commentService.countByPostId(postId);
        return ResponseEntity.ok(count);
    }
}

