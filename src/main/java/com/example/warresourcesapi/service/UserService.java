package com.example.warresourcesapi.service;

import com.example.warresourcesapi.model.ApiUser;
import com.example.warresourcesapi.model.request.UserCreateRequest;
import com.example.warresourcesapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public ApiUser readUserByUsername (String username) {
        return userRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);
    }

    public void createUser(UserCreateRequest userCreateRequest) {
        ApiUser apiUser = new ApiUser();
        Optional<ApiUser> byUsername = userRepository.findByUsername(userCreateRequest.getUsername());
        var byEmail = userRepository.findByEmail(userCreateRequest.getEmail());
        if (byEmail.isPresent()) {
            throw new RuntimeException("User already registered. Please use different email.");
        }
        apiUser.setUsername(userCreateRequest.getUsername());
        apiUser.setPassword(passwordEncoder.encode(userCreateRequest.getPassword()));
        apiUser.setRole(userCreateRequest.getRole());
        apiUser.setEmail(userCreateRequest.getEmail());
        userRepository.save(apiUser);
    }
}
