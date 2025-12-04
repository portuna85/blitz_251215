package com.blitz.springboot.domain.posts;

import com.blitz.springboot.common.BaseEntity;
import com.blitz.springboot.domain.comment.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @Column(nullable = false)
    private String authorEmail;

    @Column(nullable = false)
    private Long viewCount;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Posts(String title, String content, String author, String authorEmail) {
        validateTitle(title);
        validateContent(content);
        validateAuthor(author);
        validateAuthorEmail(authorEmail);

        this.title = title;
        this.content = content;
        this.author = author;
        this.authorEmail = authorEmail;
        this.viewCount = 0L;
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

    /**
     * 해당 사용자가 게시글의 작성자인지 확인
     *
     * @param userEmail 확인할 사용자의 이메일
     * @return 작성자인 경우 true, 아닌 경우 false
     */
    public boolean isAuthor(String userEmail) {
        if (userEmail == null) {
            return false;
        }
        return this.authorEmail.equals(userEmail);
    }

    /**
     * 조회수 증가
     */
    public void incrementViewCount() {
        this.viewCount++;
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

    private void validateAuthorEmail(String authorEmail) {
        if (authorEmail == null || authorEmail.isBlank()) {
            throw new IllegalArgumentException("작성자 이메일은 필수입니다");
        }
    }
}
