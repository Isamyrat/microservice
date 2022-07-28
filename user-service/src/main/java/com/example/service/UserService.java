package com.example.service;


import com.example.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> findAll();

    UserDto saveUser(UserDto userDto);

    UserDto updateUser(UserDto userDto, Long id);

    UserDto getUser(Long id);
}
