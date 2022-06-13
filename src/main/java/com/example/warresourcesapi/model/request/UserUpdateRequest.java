package com.example.warresourcesapi.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
//TODO: zmienić nazwy na ludzkie
public class UserUpdateRequest {
    private String username;
    private String password;
    private String old_password;
    private String r_password;
    private String email;
}
