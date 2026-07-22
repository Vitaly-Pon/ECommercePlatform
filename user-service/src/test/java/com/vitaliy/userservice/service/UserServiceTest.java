package com.vitaliy.userservice.service;

import com.vitaliy.userservice.domain.User;
import com.vitaliy.userservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldCreateAndGetUser() {
        User user = User.builder()
                .id(1L)
                .email("test@test.com")
                .role("USER")
                .build();
        userRepository.save(user);

        User found = userService.getById(1L);

        assertThat(found.getEmail()).isEqualTo("test@test.com");
    }
    @Test
    void shouldGetAllUsers() {
        userRepository.save(User.builder().id(2L).email("a@a.com").role("USER").build());
        userRepository.save(User.builder().id(3L).email("b@b.com").role("ADMIN").build());

        assertThat(userService.getAll().size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    void shouldUpdateRole() {
        User user = User.builder().id(10L).email("role@test.com").role("USER").build();
        userRepository.save(user);

        User updated = userService.updateRole(10L, "ADMIN");

        assertThat(updated.getRole()).isEqualTo("ADMIN");
    }

    @Test
    void shouldDeleteUser() {
        User user = User.builder().id(20L).email("delete@test.com").role("USER").build();
        userRepository.save(user);

        userService.delete(20L);

        assertThat(userRepository.findById(20L)).isEmpty();
    }

}
