package com.example.ananas.controller;

import com.example.ananas.dto.request.*;
import com.example.ananas.dto.response.ApiResponse;
import com.example.ananas.dto.response.AuthenticationResponse;
import com.example.ananas.dto.response.UserResponse;
import com.example.ananas.entity.Review;
import com.example.ananas.service.Service.AuthenticationService;
import com.example.ananas.service.Service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/user")
public class UserController {
    UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping
    public ApiResponse<UserResponse> addUser(@RequestBody UserCreateRequest userCreateRequest) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(userCreateRequest))
                .code(200)
                .build();
    }

    @GetMapping
    public ApiResponse<List<UserResponse>> getAllUsers() {
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getAllUsers())
                .code(200)
                .build();
    }

    @GetMapping({"{id}"})
    public ApiResponse<UserResponse> getUserById(@PathVariable int id) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUserbyId(id))
                .code(200)
                .build();
    }

    @PutMapping
    public ApiResponse<UserResponse> updateUser(@RequestParam int id, @RequestBody UserUpdateRequest userUpdateRequest) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(id, userUpdateRequest))
                .code(200)
                .build();
    }

    @PutMapping("/photo/{id}")
    public ApiResponse<UserResponse> updateUserPhoto(@PathVariable("id") int id,@RequestParam("avatar") MultipartFile file) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.uploadAvatar(id, file))
                .code(200)
                .build();
    }

    @DeleteMapping
    public ApiResponse<String> deleteUser(@RequestParam int id) {
        return ApiResponse.<String>builder()
                .result(userService.deleteUser(id))
                .code(200)
                .build();
    }

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> createToken(@RequestBody AuthenticationRequest authenticationRequest) {
        return ApiResponse.<AuthenticationResponse>builder()
                .result(authenticationService.authenticationResponse(authenticationRequest))
                .code(200)
                .build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        userService.forgotPassword(forgotPasswordRequest.getEmail());
        return ResponseEntity.ok("Verification code sent to your email.");
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> resetPassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        boolean isReset = userService.changePassword(changePasswordRequest);
        if (isReset) {
            return ResponseEntity.ok("Password has been reset successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid verification code or password.");
        }
    }
}
