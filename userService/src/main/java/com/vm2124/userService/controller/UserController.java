package com.vm2124.userService.controller;

import com.vm2124.userService.dto.UserInfoDto;
import com.vm2124.userService.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class UserController {

    private final UserInfoService userInfoService;

    @GetMapping("/{userId}/info")
    public ResponseEntity<UserInfoDto> getUserInfo(@PathVariable String userId) {
        log.info("Fetching comprehensive user information for user ID: {}", userId);
        
        try {
            UserInfoDto userInfo = userInfoService.getUserInfo(userId);
            return ResponseEntity.ok(userInfo);
            
        } catch (Exception e) {
            log.error("Error fetching user info for {}: {}", userId, e.getMessage(), e);
            return ResponseEntity.ok(new UserInfoDto(false, "Error fetching user information: " + e.getMessage(), null, null, null, null));
        }
    }
}
