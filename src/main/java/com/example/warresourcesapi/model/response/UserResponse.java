package com.example.warresourcesapi.model.response;

import com.example.warresourcesapi.model.Role;
import lombok.Data;

import java.util.Collection;

@Data
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private Collection<Role> roles;

    public UserResponse(Long id, String username, String email, Collection<Role> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}
