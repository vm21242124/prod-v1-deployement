package com.vm2124.userService.service;

import com.vm2124.userService.dto.LoginRequest;
import com.vm2124.userService.dto.LoginResponse;

public interface AuthenticationService {
    LoginResponse login(LoginRequest loginRequest);
}
