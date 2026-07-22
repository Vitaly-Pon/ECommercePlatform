package com.vitaliy.authservice.repository;

import com.vitaliy.authservice.configForTest.BaseIntegrationTest;
import com.vitaliy.authservice.domain.User;
import com.vitaliy.authservice.kafka.UserProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class UserRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private UserProducer userProducer;


    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }


    @Test
    void shouldFindUserByEmail() {

        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("123456");
        userRepository.save(user);


        Optional<User> result =
                userRepository.findByEmail("test@test.com");


        assertThat(result).isPresent();
        assertThat(result.get().getEmail())
                .isEqualTo("test@test.com");
    }
}