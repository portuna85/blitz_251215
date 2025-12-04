package com.blitz.springboot.domain.comment;

import com.blitz.springboot.common.BaseEntity;
import com.blitz.springboot.domain.posts.Posts;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String authorEmail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Posts post;

    @Builder
    public Comment(String content, String author, String authorEmail, Posts post) {
        validateContent(content);
        validateAuthor(author);
        validateAuthorEmail(authorEmail);
        validatePost(post);

        this.content = content;
        this.author = author;
        this.authorEmail = authorEmail;
        this.post = post;
    }

    public void update(String content) {
        validateContent(content);
        this.content = content;
    }

    public boolean isAuthor(String userEmail) {
        if (userEmail == null) {
            return false;
        }
        return this.authorEmail.equals(userEmail);
    }

    private void validateContent(String content) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("댓글 내용은 필수입니다");
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

    private void validatePost(Posts post) {
        if (post == null) {
            throw new IllegalArgumentException("게시글은 필수입니다");
        }
    }
}

