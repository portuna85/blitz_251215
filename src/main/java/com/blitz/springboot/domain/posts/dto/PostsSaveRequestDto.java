package com.blitz.springboot.domain.posts.dto;

import com.blitz.springboot.domain.posts.Posts;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostsSaveRequestDto {

    @NotBlank(message = "제목은 필수입니다")
    @Size(max = 500, message = "제목은 500자를 초과할 수 없습니다")
    private String title;

    @NotBlank(message = "내용은 필수입니다")
    private String content;

    private String author;
    private String authorEmail;

    @Builder
    public PostsSaveRequestDto(String title, String content, String author, String authorEmail) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.authorEmail = authorEmail;
    }

    public PostsSaveRequestDto withAuthor(String author, String authorEmail) {
        if (author == null || authorEmail == null) {
            throw new IllegalArgumentException("작성자 정보는 필수입니다");
        }
        return PostsSaveRequestDto.builder()
                .title(this.title)
                .content(this.content)
                .author(author)
                .authorEmail(authorEmail)
                .build();
    }

    public Posts toEntity() {
        return Posts.builder()
                .title(title)
                .content(content)
                .author(author)
                .authorEmail(authorEmail)
                .build();
    }
}
