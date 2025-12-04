package com.blitz.springboot.domain.posts;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostsRepository extends JpaRepository<Posts, Long> {

    @Query("SELECT p FROM Posts p ORDER BY p.id DESC")
    List<Posts> findAllDesc();

    @Query("SELECT p FROM Posts p WHERE p.title LIKE %:keyword% OR p.content LIKE %:keyword% ORDER BY p.id DESC")
    Page<Posts> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM Posts p WHERE p.authorEmail = :authorEmail ORDER BY p.id DESC")
    List<Posts> findByAuthorEmail(@Param("authorEmail") String authorEmail);

    @Query("SELECT p FROM Posts p ORDER BY p.viewCount DESC")
    List<Posts> findTopByViewCount(Pageable pageable);

    Page<Posts> findAll(Pageable pageable);
}
