package org.example.expert.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.common.dto.CustomUserDetails;
import org.example.expert.domain.user.dto.request.UserChangePasswordRequest;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable long userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @GetMapping("/users")
    public ResponseEntity<UserResponse> searchUser(@RequestParam String nickname){
        return ResponseEntity.ok(userService.searchUser(nickname));
    }

    @PutMapping("/users")
    public void changePassword(@AuthenticationPrincipal CustomUserDetails userDetails,
                               @RequestBody UserChangePasswordRequest userChangePasswordRequest) {
        userService.changePassword(userDetails.getId(), userChangePasswordRequest);
    }
}
