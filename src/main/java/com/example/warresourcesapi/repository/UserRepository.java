package com.example.warresourcesapi.repository;

import com.example.warresourcesapi.model.ApiUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<ApiUser, Long> {
    Optional<List<ApiUser>> findByUsername(String username);
    Optional<ApiUser> findByEmail(String email);
}
