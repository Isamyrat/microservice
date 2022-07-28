package com.example.service.impl;

import com.example.dto.UserDto;
import com.example.entity.RoleEntity;
import com.example.entity.UserEntity;
import com.example.exception.BadRequestException;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new BadRequestException("User:" + username + " not found."));
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        if (isUsernameExist(userDto.getUsername())) {
            throw new BadRequestException("Username is occupied. Please change it");
        }
        UserEntity user = fillingInUserData(userDto);
        userRepository.save(user);
        return userDto;
    }

    private UserEntity fillingInUserData(UserDto userDto) {
        UserEntity user = new UserEntity();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(getRole(userDto.getRoleName()));
        return user;
    }


    private boolean isUsernameExist(String userName) {
        return userRepository.existsByUsername(userName);
    }

    private RoleEntity getRole(String role) {
        return roleRepository.findByName(role)
            .orElseThrow(() -> new BadRequestException("Role:" + role + " not found."));
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long id) {
        UserEntity user = findUserById(id);

        if (!user.getUsername().equals(userDto.getUsername())) {
            if (isUsernameExist(userDto.getUsername())) {
                throw new BadRequestException("Username is occupied. Please change it");
            }
        }
        UserEntity userEntity = fillingInUserData(userDto);

        userEntity.setId(user.getId());
        userRepository.save(userEntity);
        return userDto;
    }

    @Override
    public UserDto getUser(Long id) {
        UserEntity user = findUserById(id);
        return mapUserToDto(user);
    }

    private UserDto mapUserToDto(UserEntity user) {
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setRoleName(user.getRole().getName());
        return userDto;
    }

    @Override
    public List<UserDto> findAll() {
        List<UserEntity> userList = userRepository.findAll();
        return userList
            .stream()
            .map(this::mapUserToDto)
            .collect(Collectors.toList());
    }

    private UserEntity findUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new BadRequestException("Can not find user with this id: " + id));
    }
}
