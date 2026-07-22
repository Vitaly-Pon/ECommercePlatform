package com.vitaliy.userservice.repository;

import com.vitaliy.userservice.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
