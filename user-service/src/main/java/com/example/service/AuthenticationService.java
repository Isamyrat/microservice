package com.example.service;

import com.example.dto.JwtRequestDto;
import com.example.dto.JwtResponseDto;

public interface AuthenticationService {
    JwtResponseDto login(JwtRequestDto jwtRequest);
}
