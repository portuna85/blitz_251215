package com.blitz.springboot.domain.comment.dto;

import com.blitz.springboot.domain.comment.Comment;
import com.blitz.springboot.domain.posts.Posts;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentSaveRequestDto {

    @NotBlank(message = "댓글 내용은 필수입니다")
    private String content;

    private String author;
    private String authorEmail;

    @Builder
    public CommentSaveRequestDto(String content, String author, String authorEmail) {
        this.content = content;
        this.author = author;
        this.authorEmail = authorEmail;
    }

    public Comment toEntity(Posts post) {
        return Comment.builder()
                .content(content)
                .author(author)
                .authorEmail(authorEmail)
                .post(post)
                .build();
    }
}

