package com.blitz.springboot.web;

import com.blitz.springboot.service.PostsService;
import com.blitz.springboot.web.dto.PostsListResponseDto;
import com.blitz.springboot.web.dto.PostsResponseDto;
import com.blitz.springboot.web.dto.PostsSaveRequestDto;
import com.blitz.springboot.web.dto.PostsUpdateRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/posts")
public class PostsApiController {

    private final PostsService postsService;

    @PostMapping
    public ResponseEntity<Long> save(@Valid @RequestBody PostsSaveRequestDto requestDto) {
        Long savedId = postsService.save(requestDto);
        return ResponseEntity.created(URI.create("/api/v1/posts/" + savedId))
                .body(savedId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> update(
            @PathVariable Long id,
            @Valid @RequestBody PostsUpdateRequestDto requestDto) {
        Long updatedId = postsService.update(id, requestDto);
        return ResponseEntity.ok(updatedId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        postsService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostsResponseDto> findById(@PathVariable Long id) {
        PostsResponseDto response = postsService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PostsListResponseDto>> findAll() {
        List<PostsListResponseDto> response = postsService.findAllDesc();
        return ResponseEntity.ok(response);
    }
}
