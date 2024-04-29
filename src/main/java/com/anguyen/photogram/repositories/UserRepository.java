package com.anguyen.photogram.repositories;

import com.anguyen.photogram.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByIdAndStatusTrue(String id);

    Optional<UserEntity> findByUsernameAndStatusTrue(String username);

    Boolean existsByUsername(String username);
}
