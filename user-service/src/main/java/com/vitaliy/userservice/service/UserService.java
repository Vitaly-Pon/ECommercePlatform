package com.vitaliy.userservice.service;

import com.vitaliy.userservice.domain.User;
import com.vitaliy.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public List<User> getAll() {
        return  repository.findAll();
    }

    public User getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User updateRole(Long id, String role) {
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setRole(role);
        return repository.save(user);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

}
