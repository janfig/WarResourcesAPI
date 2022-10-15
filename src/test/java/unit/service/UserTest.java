package unit.service;

import com.example.warresourcesapi.exception.BadRequestException;
import com.example.warresourcesapi.exception.NotFoundException;
import com.example.warresourcesapi.model.AppUser;
import com.example.warresourcesapi.model.Role;
import com.example.warresourcesapi.model.request.UserCreateRequest;
import com.example.warresourcesapi.model.request.UserUpdateRequest;
import com.example.warresourcesapi.model.response.UserResponse;
import com.example.warresourcesapi.repository.RoleRepository;
import com.example.warresourcesapi.repository.UserRepository;
import com.example.warresourcesapi.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserTest {
    private UserRepository userRepository = null;
    private RoleRepository roleRepository = null;
    private PasswordEncoder passwordEncoder = null;
    private UserService userService = null;

    @BeforeEach
    void setup() {
        this.userRepository = Mockito.mock(UserRepository.class);
        this.roleRepository = Mockito.mock(RoleRepository.class);
        this.passwordEncoder = Mockito.mock(PasswordEncoder.class);

        this.userService = new UserService(
                this.userRepository,
                this.roleRepository,
                this.passwordEncoder
        );
    }

    @Test
    void loadUserByUsername_userExists_returnsUserDetails() {
        // arrange
        Collection<Role> roles = new ArrayList<>();
        roles.add(new Role("PREMIUM"));
        AppUser user = new AppUser("zbych", "pass", "zbych@email.com", roles);
        user.setId(1L);

        when(this.userRepository.findByEmail(user.getEmail())).thenReturn(user);

        // act
        var result = this.userService.loadUserByUsername(user.getEmail());

        // assert
        assertEquals(user, result);
    }

    @Test
    void loadUserByUsername_userNotExists_returnsUserDetails() {
        // arrange
        when(this.userRepository.findByEmail(any())).thenReturn(null);

        // act nad assert
        assertThrows(UsernameNotFoundException.class, () -> this.userService.loadUserByUsername("bad_email"));
    }

    @Test
    void saveUser_properData_returnsAppUser() {
        // arrange
        Collection<Role> roles = new ArrayList<>();
        roles.add(new Role("BASIC"));
        roles.add(new Role("PREMIUM"));
        AppUser user = new AppUser("zbych", "pass", "zbych@email.com", roles);
        UserCreateRequest request = new UserCreateRequest();
        request.setEmail(user.getEmail());
        request.setUsername(user.getUsername());
        request.setEmail(user.getEmail());
        request.setRoleName("PREMIUM");

        when(this.userRepository.findByEmail(user.getEmail())).thenReturn(null);
        when(this.userRepository.save(any())).thenAnswer((i -> i.getArguments()[0]));
        when(this.passwordEncoder.encode(request.getPassword())).thenReturn(user.getPassword());
        when(this.roleRepository.findByName("BASIC")).thenReturn(new Role("BASIC"));
        when(this.roleRepository.findByName("PREMIUM")).thenReturn(new Role("PREMIUM"));

        // act
        var result = this.userService.saveUser(request);

        // assert
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getPassword(), result.getPassword());
        assertArrayEquals(user.getRoles().toArray(), result.getRoles().toArray());
    }

    @Test
    void saveUser_repeatedEmail_throwsBadRequestException() {
        // arrange
        Collection<Role> roles = new ArrayList<>();
        roles.add(new Role("BASIC"));
        roles.add(new Role("PREMIUM"));
        AppUser user = new AppUser("zbych", "pass", "zbych@email.com", roles);
        UserCreateRequest request = new UserCreateRequest();
        request.setEmail(user.getEmail());
        request.setUsername(user.getUsername());
        request.setEmail(user.getEmail());
        request.setRoleName("PREMIUM");

        when(this.userRepository.findByEmail(user.getEmail())).thenReturn(user);
        when(this.userRepository.save(any())).thenAnswer((i -> i.getArguments()[0]));
        when(this.passwordEncoder.encode(request.getPassword())).thenReturn(user.getPassword());
        when(this.roleRepository.findByName("BASIC")).thenReturn(new Role("BASIC"));
        when(this.roleRepository.findByName("PREMIUM")).thenReturn(new Role("PREMIUM"));

        // act
        assertThrows(BadRequestException.class, () -> this.userService.saveUser(request));
    }

    @Test
    void saveUser_badRole_throwsBadRequestException() {
        // arrange
        Collection<Role> roles = new ArrayList<>();
        roles.add(new Role("BASIC"));
        roles.add(new Role("PREMIUM"));
        AppUser user = new AppUser("zbych", "pass", "zbych@email.com", roles);
        UserCreateRequest request = new UserCreateRequest();
        request.setEmail(user.getEmail());
        request.setUsername(user.getUsername());
        request.setEmail(user.getEmail());
        request.setRoleName("BAD ROLE");

        when(this.userRepository.findByEmail(user.getEmail())).thenReturn(null);
        when(this.userRepository.save(any())).thenAnswer((i -> i.getArguments()[0]));
        when(this.passwordEncoder.encode(request.getPassword())).thenReturn(user.getPassword());
        when(this.roleRepository.findByName("BASIC")).thenReturn(new Role("BASIC"));
        when(this.roleRepository.findByName("BAD ROLE")).thenReturn(null);

        // act
        assertThrows(BadRequestException.class, () -> this.userService.saveUser(request));
    }

    @Test
    void getUser_userExists_returnsUserResponse() {
        // arrange
        Collection<Role> roles = new ArrayList<>();
        roles.add(new Role("BASIC"));
        AppUser user = new AppUser("zbych", "pass", "zbych@email.com", roles);
        user.setId(1L);

        when(this.userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // act
        var result = this.userService.getUser(user.getId());

        var expected_user = new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRoles());

        // assert
        assertEquals(expected_user, result);
    }

    @Test
    void getUser_userNOTExists_throwsNotFoundException() {
        // arrange
        when(this.userRepository.findById(any())).thenReturn(Optional.empty());

        // act
        assertThrows(NotFoundException.class, () -> this.userService.getUser(-1L));
    }

    @Test
    void deleteUser_userExists_notThrow() {
        // arrange
        Collection<Role> roles = new ArrayList<>();
        roles.add(new Role("BASIC"));
        AppUser user = new AppUser("zbych", "pass", "zbych@email.com", roles);
        user.setId(1L);

        when(this.userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // act and assert
        assertDoesNotThrow(() -> this.userService.deleteUser(user.getId()));
    }

    @Test
    void deleteUser_userNOTExists_throwNotFoundException() {
        // arrange
        when(this.userRepository.findById(any())).thenReturn(Optional.empty());

        // act
        assertThrows(NotFoundException.class, () -> this.userService.deleteUser(-1L));
    }

    @Test
    void updateUser_userExists_notThrow() {
        // arrange
        var new_password = "new password";
        var new_email = "zbych2@email.com";
        var new_username = "zbych2";
        Collection<Role> roles = new ArrayList<>();
        roles.add(new Role("BASIC"));
        roles.add(new Role("PREMIUM"));
        AppUser user = new AppUser("zbych", "pass", "zbych@email.com", roles);
        UserUpdateRequest request = new UserUpdateRequest();
        request.setEmail(new_email);
        request.setUsername(new_username);
        request.setPassword(new_password);
        request.setOld_password(user.getPassword());

        when(this.userRepository.getById(user.getId())).thenReturn(user);
        when(this.userRepository.save(any())).thenAnswer((i -> i.getArguments()[0]));
        when(this.passwordEncoder.encode(request.getPassword())).thenReturn(new_password);
        when(this.passwordEncoder.matches(request.getOld_password(), user.getPassword())).thenReturn(request.getOld_password().equals(user.getPassword()));

        // act
        var result = this.userService.updateUser(user.getId(), request);

        // assert
        assertEquals(new_email, result.getEmail());
        assertEquals(new_username, result.getUsername());
        assertEquals(new_password, result.getPassword());
        assertArrayEquals(user.getRoles().toArray(), result.getRoles().toArray());
    }

    @Test
    void updateUser_userExists_bad_password_throwNotFoundException() {
        // arrange
        var new_password = "new password";
        var new_email = "zbych2@email.com";
        var new_username = "zbych2";
        Collection<Role> roles = new ArrayList<>();
        roles.add(new Role("BASIC"));
        roles.add(new Role("PREMIUM"));
        AppUser user = new AppUser("zbych", "pass", "zbych@email.com", roles);
        UserUpdateRequest request = new UserUpdateRequest();
        request.setEmail(new_email);
        request.setUsername(new_username);
        request.setPassword(new_password);
        request.setOld_password(new_password);

        when(this.userRepository.getById(user.getId())).thenReturn(user);
        when(this.userRepository.save(any())).thenAnswer((i -> i.getArguments()[0]));
        when(this.passwordEncoder.encode(request.getPassword())).thenReturn(new_password);
        when(this.passwordEncoder.matches(request.getOld_password(), user.getPassword())).thenReturn(request.getOld_password().equals(user.getPassword()));

        // act
        assertThrows(BadRequestException.class, () -> this.userService.updateUser(user.getId(), request));
    }
}
