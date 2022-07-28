package com.example.service;

import com.example.dto.JwtRequestDto;
import com.example.dto.JwtResponseDto;
import com.example.entity.UserEntity;

public interface AuthenticationService {
    JwtResponseDto login(JwtRequestDto jwtRequest);
    UserEntity getPrincipal();
}
