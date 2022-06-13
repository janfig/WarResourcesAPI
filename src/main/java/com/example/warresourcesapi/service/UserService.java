package com.example.warresourcesapi.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.warresourcesapi.exception.BadRequestException;
import com.example.warresourcesapi.exception.ForbiddenRequestException;
import com.example.warresourcesapi.exception.NotFoundException;
import com.example.warresourcesapi.model.AppUser;
import com.example.warresourcesapi.model.Role;
import com.example.warresourcesapi.model.request.UserCreateRequest;
import com.example.warresourcesapi.model.request.UserUpdateRequest;
import com.example.warresourcesapi.model.response.UserResponse;
import com.example.warresourcesapi.repository.RoleRepository;
import com.example.warresourcesapi.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser user = userRepository.findByEmail(email);
        if(user == null) {
            log.error("User not found in the db");
            throw new UsernameNotFoundException("User not found in the db");
        } else {
            log.info("User found in the db: {}", email);
        }
        return user;
    }

    public AppUser saveUser(UserCreateRequest request) {
        if(userRepository.findByEmail(request.getEmail()) != null)
            throw new BadRequestException("This email is taken");

        var user = new AppUser();
        var encodedPassword = passwordEncoder.encode(request.getPassword());
        user.setUsername(request.getUsername());
        user.setPassword(encodedPassword);
        user.setEmail(request.getEmail());

        Role role = roleRepository.findByName("STANDARD");
        user.addRole(role);

        if(request.getRoleName() != null) {
            Role requestedRole = roleRepository.findByName(request.getRoleName());
            if (requestedRole == null) {
                throw new BadRequestException("Role with specified name does not exist");
            }
            user.addRole(requestedRole);
        }

        log.info("Saving new user {} to db", user.getUsername());
        return userRepository.save(user);
    }

    public Role saveRole(Role role) {
        log.info("Saving new role {} to db", role.getName());
        return roleRepository.save(role);
    }

    public void addRoleToUser(String email, String roleName) {
        log.info("Adding role {} to user with email: {}", roleName, email);
        AppUser user = userRepository.findByEmail(email);
        Role role = roleRepository.findByName(roleName);
        user.addRole(role);
    }

    public UserResponse getUser(Long id) {
                log.info("Fetching user with id: {}", id);
                var repoUser = userRepository.findById(id);
                if (repoUser.isEmpty()) {
                    throw new NotFoundException("Given user does not exist");
                }
                var user = repoUser.get();
                return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRoles());
    }

    public List<AppUser> getUsers() {
        log.info("Fetching all users");
        return userRepository.findAll();
    }

    public void deleteUser(Long id) {
        log.info("Deleting user with id: {}", id);
        if(!userRepository.findById(id).isPresent())
            throw new NotFoundException("Given user does not exist");
        userRepository.deleteById(id);
    }

    public Long getAuthId() {
        return Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
    }

    public AppUser updateUser(Long id, UserUpdateRequest request) {
        log.info("Updating user with id: {}", id);
        AppUser user = userRepository.getById(id);
        if(!request.getEmail().equals(""))
            user.setEmail(request.getEmail());
        if (!request.getUsername().equals(""))
            user.setUsername(request.getUsername());
        if (!request.getPassword().equals("")) {
            var encodedPassword = passwordEncoder.encode(request.getPassword());
            if(!passwordEncoder.matches(request.getOld_password(), user.getPassword()))
                throw new BadRequestException("Old password is incorrect");
            user.setPassword(encodedPassword);
        }
        return userRepository.save(user);
    }
}
