package com.blitz.springboot.domain.comment.service;

import com.blitz.springboot.common.exception.EntityNotFoundException;
import com.blitz.springboot.common.exception.ErrorCode;
import com.blitz.springboot.common.exception.UnauthorizedException;
import com.blitz.springboot.domain.comment.Comment;
import com.blitz.springboot.domain.comment.CommentRepository;
import com.blitz.springboot.domain.comment.dto.CommentResponseDto;
import com.blitz.springboot.domain.comment.dto.CommentSaveRequestDto;
import com.blitz.springboot.domain.comment.dto.CommentUpdateRequestDto;
import com.blitz.springboot.domain.posts.Posts;
import com.blitz.springboot.domain.posts.PostsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostsRepository postsRepository;

    @Override
    @Transactional
    public Long save(Long postId, CommentSaveRequestDto requestDto) {
        Posts post = findPostById(postId);

        Comment comment = requestDto.toEntity(post);

        Comment savedComment = commentRepository.save(comment);
        log.info("댓글 저장 완료: id={}, postId={}, author={}",
                savedComment.getId(), postId, savedComment.getAuthor());
        return savedComment.getId();
    }

    @Override
    @Transactional
    public Long update(Long commentId, CommentUpdateRequestDto requestDto, String userEmail) {
        Comment comment = findCommentById(commentId);
        validateAuthor(comment, userEmail);

        comment.update(requestDto.getContent());
        log.info("댓글 수정 완료: id={}, userEmail={}", commentId, userEmail);
        return commentId;
    }

    @Override
    @Transactional
    public void delete(Long commentId, String userEmail) {
        Comment comment = findCommentById(commentId);
        validateAuthor(comment, userEmail);

        commentRepository.delete(comment);
        log.info("댓글 삭제 완료: id={}, userEmail={}", commentId, userEmail);
    }

    @Override
    @Transactional(readOnly = true)
    public CommentResponseDto findById(Long commentId) {
        Comment comment = findCommentById(commentId);
        return new CommentResponseDto(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponseDto> findByPostId(Long postId) {
        List<CommentResponseDto> comments = commentRepository
                .findByPostIdOrderByCreatedDateAsc(postId)
                .stream()
                .map(CommentResponseDto::new)
                .toList();
        log.debug("게시글 댓글 조회 완료: postId={}, count={}", postId, comments.size());
        return comments;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponseDto> findMyComments(String userEmail) {
        if (userEmail == null || userEmail.isBlank()) {
            throw new IllegalArgumentException("이메일은 필수입니다");
        }

        List<CommentResponseDto> comments = commentRepository
                .findByAuthorEmailOrderByCreatedDateDesc(userEmail)
                .stream()
                .map(CommentResponseDto::new)
                .toList();
        log.debug("내 댓글 조회 완료: userEmail={}, count={}", userEmail, comments.size());
        return comments;
    }

    @Override
    @Transactional(readOnly = true)
    public Long countByPostId(Long postId) {
        return commentRepository.countByPostId(postId);
    }

    private Posts findPostById(Long postId) {
        return postsRepository.findById(postId)
                .orElseThrow(() -> {
                    log.warn("게시글을 찾을 수 없음: id={}", postId);
                    return new EntityNotFoundException(
                            ErrorCode.POST_NOT_FOUND,
                            "게시글을 찾을 수 없습니다. id=" + postId
                    );
                });
    }

    private Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> {
                    log.warn("댓글을 찾을 수 없음: id={}", commentId);
                    return new EntityNotFoundException(
                            ErrorCode.COMMENT_NOT_FOUND,
                            "댓글을 찾을 수 없습니다. id=" + commentId
                    );
                });
    }

    private void validateAuthor(Comment comment, String userEmail) {
        if (userEmail == null) {
            log.warn("로그인하지 않은 사용자의 댓글 수정/삭제 시도: commentId={}", comment.getId());
            throw new UnauthorizedException(
                    ErrorCode.UNAUTHORIZED_COMMENT_ACCESS,
                    "로그인이 필요합니다."
            );
        }

        if (!comment.isAuthor(userEmail)) {
            log.warn("권한 없는 사용자의 댓글 수정/삭제 시도: commentId={}, userEmail={}, author={}",
                    comment.getId(), userEmail, comment.getAuthor());
            throw new UnauthorizedException(
                    ErrorCode.UNAUTHORIZED_COMMENT_ACCESS,
                    "댓글을 수정/삭제할 권한이 없습니다."
            );
        }
    }
}

