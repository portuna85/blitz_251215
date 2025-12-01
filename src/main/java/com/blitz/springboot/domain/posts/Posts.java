package com.blitz.springboot.domain.posts;

import com.blitz.springboot.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * 게시글 엔티티
 * SRP: 게시글 도메인 로직과 데이터를 관리
 * OCP: 비즈니스 요구사항 변경 시 확장 가능하도록 설계
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Posts extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    private String author;

    @Builder
    public Posts(String title, String content, String author) {
        validateTitle(title);
        validateContent(content);
        validateAuthor(author);

        this.title = title;
        this.content = content;
        this.author = author;
    }

    /**
     * 게시글 수정
     *
     * @param title 수정할 제목
     * @param content 수정할 내용
     * @throws IllegalArgumentException 유효하지 않은 입력값인 경우
     */
    public void update(String title, String content) {
        validateTitle(title);
        validateContent(content);

        this.title = title;
        this.content = content;
    }

    private void validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("제목은 필수입니다");
        }
        if (title.length() > 500) {
            throw new IllegalArgumentException("제목은 500자를 초과할 수 없습니다");
        }
    }

    private void validateContent(String content) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("내용은 필수입니다");
        }
    }

    private void validateAuthor(String author) {
        if (author == null || author.isBlank()) {
            throw new IllegalArgumentException("작성자는 필수입니다");
        }
    }
}
