package com.example.user.service;

import com.example.user.dto.PaginationResponseDto;
import com.example.user.dto.RoleDto;
import com.example.user.entity.Role;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;



public interface RoleService {
    ResponseEntity<Role> saveOrUpdateRole(RoleDto roleDto);

    ResponseEntity<PaginationResponseDto<Role>> getRoles(Long roleId, String roleName, String description, String status, Pageable pageable);

    ResponseEntity<Role> getRoleById(Long roleId);
}
