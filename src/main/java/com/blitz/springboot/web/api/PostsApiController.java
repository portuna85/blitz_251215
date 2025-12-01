package com.blitz.springboot.web.api;

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
    public ResponseEntity<Long> save(@Valid @RequestBody PostsSaveRequestDto requestDto) {
        log.info("게시글 생성 요청: title={}, author={}", requestDto.getTitle(), requestDto.getAuthor());
        Long savedId = postsService.save(requestDto);
        log.info("게시글 생성 완료: id={}", savedId);
        return ResponseEntity.created(URI.create("/api/v1/posts/" + savedId))
                .body(savedId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> update(@PathVariable Long id,
                                        @Valid @RequestBody PostsUpdateRequestDto requestDto) {
        log.info("게시글 수정 요청: id={}, title={}", id, requestDto.getTitle());
        Long updatedId = postsService.update(id, requestDto);
        log.info("게시글 수정 완료: id={}", updatedId);
        return ResponseEntity.ok(updatedId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("게시글 삭제 요청: id={}", id);
        postsService.delete(id);
        log.info("게시글 삭제 완료: id={}", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostsResponseDto> findById(@PathVariable Long id) {
        log.debug("게시글 조회 요청: id={}", id);
        PostsResponseDto response = postsService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PostsListResponseDto>> findAll() {
        log.debug("게시글 목록 조회 요청");
        List<PostsListResponseDto> response = postsService.findAllDesc();
        log.debug("게시글 목록 조회 완료: count={}", response.size());
        return ResponseEntity.ok(response);
    }
}
