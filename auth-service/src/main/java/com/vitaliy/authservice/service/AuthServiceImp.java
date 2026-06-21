package com.vitaliy.authservice.service;

import com.vitaliy.authservice.domain.User;
import com.vitaliy.authservice.dto.request.LoginRequest;
import com.vitaliy.authservice.dto.request.RegisterRequest;
import com.vitaliy.authservice.dto.response.AuthResponse;
import com.vitaliy.authservice.dto.response.UserResponse;
import com.vitaliy.authservice.exeption.EmailAlreadyExistsException;
import com.vitaliy.authservice.repository.UserRepository;
import com.vitaliy.authservice.config.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImp implements AuthService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImp(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
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

        // 🔴 КРИТИЧЕСКИЙ ЛОГ: смотрим, какой именно класс кодировщика используется,
        // что прислал клиент и что лежит в базе данных
        System.out.println("=== КРИТИЧЕСКИЙ ТЕСТ ПАРОЛЯ ===");
        System.out.println("Используемый класс кодера: " + passwordEncoder.getClass().getName());
        System.out.println("Пароль из Postman: " + request.getPassword());
        System.out.println("Хэш из базы данных: " + user.getPassword());

        boolean isMatches = passwordEncoder.matches(request.getPassword(), user.getPassword());
        System.out.println("Результат проверки matches(): " + isMatches);
        System.out.println("===============================");

        if (!isMatches) {
            throw new RuntimeException("Invalid password");
        }

        return new AuthResponse(
                jwtService.generateToken(user),
                user.getId(),
                user.getEmail(),
                user.getRole().name()
        );
    }
}
