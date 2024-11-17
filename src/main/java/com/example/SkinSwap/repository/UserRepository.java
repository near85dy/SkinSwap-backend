package com.example.SkinSwap.repository;

import com.example.SkinSwap.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findBySteamid(Long steamid);
    Optional<User> findByUsername(String username);
}

