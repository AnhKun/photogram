package com.anguyen.photogram.repositories;

import java.util.Optional;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.anguyen.photogram.entities.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByIdAndStatusTrue(String id);

    Optional<UserEntity> findByUsernameAndStatusTrue(String username);

    Optional<UserEntity> findByEmailAndStatusTrue(String username);

    Boolean existsByUsername(String username);

    @Modifying
    @Transactional
    @Query("UPDATE UserEntity u SET u.password = ?2 WHERE u.email = ?1")
    void updatePassword(String email, String password);
}
