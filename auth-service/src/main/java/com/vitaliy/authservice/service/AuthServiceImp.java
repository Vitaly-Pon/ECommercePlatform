package com.vitaliy.authservice.service;

import com.vitaliy.authservice.domain.RefreshToken;
import com.vitaliy.authservice.domain.User;
import com.vitaliy.authservice.dto.request.LoginRequest;
import com.vitaliy.authservice.dto.request.RegisterRequest;
import com.vitaliy.authservice.dto.response.AuthResponse;
import com.vitaliy.authservice.dto.response.UserResponse;
import com.vitaliy.authservice.exeption.EmailAlreadyExistsException;
import com.vitaliy.authservice.repository.RefreshTokenRepository;
import com.vitaliy.authservice.repository.UserRepository;
import com.vitaliy.authservice.config.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImp implements AuthService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthServiceImp(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, RefreshTokenRepository refreshTokenRepository){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenRepository = refreshTokenRepository;
    }
    @Override
    public  UserResponse  register(RegisterRequest registerRequest){

        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        User saved = userRepository.save(user);

        return new UserResponse(saved.getId(), saved.getEmail());
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String accessToken = jwtService.generateToken(user);
        RefreshToken refreshToken = createRefreshToken(user);

        return new AuthResponse(
                accessToken,
                refreshToken.getToken(),
                user.getId(),
                user.getEmail(),
                user.getRole().name()
        );
    }

    private String generateRefreshToken() {
        return java.util.UUID.randomUUID().toString();
    }

    private RefreshToken createRefreshToken(User user) {

        refreshTokenRepository.deleteByUser(user);

        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setToken(generateRefreshToken());
        token.setExpiryDate(java.time.Instant.now().plusSeconds(7 * 24 * 60 * 60));

        return refreshTokenRepository.save(token);
    }

    @Override
    public AuthResponse refresh(String refreshTokenValue) {

        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenValue)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        if (refreshToken.getExpiryDate().isBefore(java.time.Instant.now())) {
            throw new RuntimeException("Refresh token expired");
        }

        User user = refreshToken.getUser();

        String newAccessToken = jwtService.generateToken(user);

        return new AuthResponse(
                newAccessToken,
                refreshToken.getToken(),
                user.getId(),
                user.getEmail(),
                user.getRole().name()
        );
    }

    @Override
    public void logout(String refreshTokenValue) {

        RefreshToken token = refreshTokenRepository.findByToken(refreshTokenValue)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        refreshTokenRepository.delete(token);
    }

}
