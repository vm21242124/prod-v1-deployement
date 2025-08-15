package com.vm2124.userService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class CreateUserRequest {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class UpdateUserRequest {
    private String email;
    private String firstName;
    private String lastName;
    private String status;
}
