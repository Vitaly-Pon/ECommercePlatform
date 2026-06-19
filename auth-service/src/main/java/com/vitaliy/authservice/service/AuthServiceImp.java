package com.vitaliy.authservice.service;

import com.vitaliy.authservice.domain.User;
import com.vitaliy.authservice.dto.request.LoginRequest;
import com.vitaliy.authservice.dto.request.RegisterRequest;
import com.vitaliy.authservice.dto.response.AuthResponse;
import com.vitaliy.authservice.dto.response.UserResponse;
import com.vitaliy.authservice.exeption.EmailAlreadyExistsException;
import com.vitaliy.authservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class AuthServiceImp implements AuthService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImp(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

        if (!passwordEncoder.matches(request.getPassword(),user.getPassword())){
            throw  new RuntimeException("Invalid password");
        }
        return new AuthResponse("LOGIN_SUCCESS");
    }


}
