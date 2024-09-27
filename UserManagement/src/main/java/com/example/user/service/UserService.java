package com.example.user.service;

import com.example.user.dto.*;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    ResponseEntity<UserResponseDto> saveOrUpdateUser(UserRequestDto userRequestDto);
    ResponseEntity<PaginationResponseDto<UserResponseDto>> getUsers(Long userId, String userName, String email, String status, Pageable pageable);
    ResponseEntity<UserResponseDto> getUserById(Long userId);
    ResponseEntity<List<RoleInfoDto>> getAllRoles();
    ResponseEntity<List<GroupNameDto>> getAllGroups();
}
