package com.anguyen.photogram.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.anguyen.photogram.entities.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {
    Page<Comment> findAllByPostIdAndStatusTrue(String postId, Pageable pageable);

    Optional<Comment> findByIdAndStatusTrue(String id);
}
