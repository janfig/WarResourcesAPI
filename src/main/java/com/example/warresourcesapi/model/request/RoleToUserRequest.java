package com.example.warresourcesapi.model.request;

import lombok.Data;

@Data
public class RoleToUserRequest {
    private String email;
    private String roleName;
}
