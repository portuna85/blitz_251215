package com.blitz.springboot.domain.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c WHERE c.post.id = :postId ORDER BY c.createdDate ASC")
    List<Comment> findByPostIdOrderByCreatedDateAsc(@Param("postId") Long postId);

    @Query("SELECT c FROM Comment c WHERE c.authorEmail = :authorEmail ORDER BY c.createdDate DESC")
    List<Comment> findByAuthorEmailOrderByCreatedDateDesc(@Param("authorEmail") String authorEmail);

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.post.id = :postId")
    Long countByPostId(@Param("postId") Long postId);
}

