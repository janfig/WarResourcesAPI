package com.example.warresourcesapi.service;

import com.example.warresourcesapi.exception.BadRequestException;
import com.example.warresourcesapi.exception.NotFoundException;
import com.example.warresourcesapi.model.ApiUser;
import com.example.warresourcesapi.model.request.UserCreateRequest;
import com.example.warresourcesapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public ApiUser readUserByUsername (String username) {
        var byEmail = userRepository.findByEmail(username);
        if(byEmail.isEmpty()) {
            throw new NotFoundException("There is no user with email: " + username);
        }
        return byEmail.get();
    }

    public void createUser(UserCreateRequest userCreateRequest) {
        ApiUser apiUser = new ApiUser();
        var byEmail = userRepository.findByEmail(userCreateRequest.getEmail());
        if (byEmail.isPresent()) {
            throw new BadRequestException("User already registered. Please use different email.");
        }
        apiUser.setUsername(userCreateRequest.getUsername());
        apiUser.setPassword(passwordEncoder.encode(userCreateRequest.getPassword()));
        apiUser.setRole(userCreateRequest.getRole());
        apiUser.setEmail(userCreateRequest.getEmail());
        userRepository.save(apiUser);
    }
}
