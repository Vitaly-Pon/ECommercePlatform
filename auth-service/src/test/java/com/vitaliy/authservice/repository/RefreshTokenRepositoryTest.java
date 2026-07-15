package com.vitaliy.authservice.repository;

import com.vitaliy.authservice.configForTest.BaseIntegrationTest;
import com.vitaliy.authservice.domain.RefreshToken;
import com.vitaliy.authservice.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class RefreshTokenRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;


    @Test
    void shouldFindRefreshTokenByToken() {

        User user = new User();
        user.setEmail("refresh@test.com");
        user.setPassword("123456");

        userRepository.save(user);


        RefreshToken token = new RefreshToken();

        token.setToken("test-refresh-token");
        token.setUser(user);
        token.setExpiryDate(Instant.now().plus(7, ChronoUnit.DAYS));


        refreshTokenRepository.save(token);


        Optional<RefreshToken> result =
                refreshTokenRepository.findByToken("test-refresh-token");


        assertThat(result).isPresent();
        assertThat(result.get().getToken())
                .isEqualTo("test-refresh-token");
    }
}
