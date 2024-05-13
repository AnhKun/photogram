package com.anguyen.photogram.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.anguyen.photogram.entities.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, String> {
    Page<Post> findAllByUserIdAndStatusTrue(String userId, Pageable pageable);

    Optional<Post> findByIdAndStatusTrue(String id);
}
