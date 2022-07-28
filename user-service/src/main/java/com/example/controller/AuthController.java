package com.example.controller;

import com.example.dto.JwtRequestDto;
import com.example.dto.JwtResponseDto;
import com.example.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(AuthController.LOGIN_URL)
@RequiredArgsConstructor
public class AuthController {

    public static final String LOGIN_URL = "/api/v2/auth/login";

    private final AuthenticationService authService;

    @PostMapping
    public ResponseEntity<JwtResponseDto> login(@Valid @RequestBody JwtRequestDto jwtRequest) {
        return ResponseEntity.ok(authService.login(jwtRequest));
    }
}
