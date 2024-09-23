package com.example.user.service;

import com.example.user.dto.PaginationResponseDto;
import com.example.user.dto.RoleDto;
import com.example.user.entity.Role;
import com.example.user.enums.Status;
import com.example.user.exception.RoleAlreadyExistsException;
import com.example.user.exception.RoleNotPresetException;
import com.example.user.repository.RoleRepository;
import com.example.user.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    private RoleDto roleDto;
    private Role role;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        roleDto = RoleDto.builder()
                .roleId(1L)
                .roleName("Admin")
                .description("Administrator role")
                .updatedBy("testUser")
                .build();

        role = Role.builder()
                .roleId(1L)
                .roleName("Admin")
                .description("Administrator role")
                .status(Status.ACTIVE)
                .createdBy("testUser")
                .build();
    }

    @Test
    public void testSaveOrUpdateRole_createRole_success() {
        // Mock role doesn't exist
        when(roleRepository.findByRoleName(any())).thenReturn(Optional.empty());
        when(roleRepository.save(any())).thenReturn(role);

        ResponseEntity<Role> response = roleService.saveOrUpdateRole(roleDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(role.getRoleName(), response.getBody().getRoleName());
    }

    @Test
    public void testSaveOrUpdateRole_updateRole_success() {
        // Mock role exists for update
        roleDto.setRoleId(1L);
        when(roleRepository.findByRoleName(any())).thenReturn(Optional.empty());
        when(roleRepository.findById(anyLong())).thenReturn(Optional.of(role));
        when(roleRepository.save(any())).thenReturn(role);

        ResponseEntity<Role> response = roleService.saveOrUpdateRole(roleDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(role.getRoleName(), response.getBody().getRoleName());
    }

    @Test
    public void testCreateRole_roleAlreadyExistsException() {
        // Mock role already exists
        when(roleRepository.findByRoleName(any())).thenReturn(Optional.of(role));

        assertThrows(RoleAlreadyExistsException.class, () -> {
            roleService.saveOrUpdateRole(roleDto);
        });
    }

    @Test
    public void testUpdateRole_roleNotFoundException() {
        // Mock role not found
        roleDto.setRoleId(2L);
        when(roleRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RoleNotPresetException.class, () -> {
            roleService.saveOrUpdateRole(roleDto);
        });
    }

    @Test
    public void testGetRoleById_roleFound() {
        // Mock role found
        when(roleRepository.findById(anyLong())).thenReturn(Optional.of(role));

        ResponseEntity<Role> response = roleService.getRoleById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(role.getRoleName(), response.getBody().getRoleName());
    }

    @Test
    public void testGetRoleById_roleNotFound() {
        // Mock role not found
        when(roleRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<Role> response = roleService.getRoleById(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testGetRoles_withPagination() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Role> page = new PageImpl<>(Arrays.asList(role), pageable, 1);

        when(roleRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        ResponseEntity<PaginationResponseDto<Role>> response = roleService.getRoles(1L, "Admin", null, "ACTIVE", pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getTotalItems());
        assertEquals("Admin", response.getBody().getResponses().get(0).getRoleName());
    }
}
