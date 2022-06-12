package com.example.warresourcesapi.model.request;

import lombok.Data;

@Data
public class UserCreateRequest {
    private String username;
    private String password;
    private String roleName;
    private String email;
}
