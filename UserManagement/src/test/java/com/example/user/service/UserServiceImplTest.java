package com.example.user.service;

import com.example.user.dto.*;
import com.example.user.entity.Groups;
import com.example.user.entity.Role;
import com.example.user.entity.User;
import com.example.user.enums.Status;
import com.example.user.exception.UserAlreadyExistsException;
import com.example.user.exception.UserNotFoundException;
import com.example.user.repository.*;
import com.example.user.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private UserGroupRepository userGroupRepository;

    @Mock
    private GroupRoleRepository groupRoleRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private GroupRepository groupRepository;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private UserRequestDto userRequestDto;
    private User user;
    private Role role1;
    private Role role2;
    private Groups group1;
    private Groups group2;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userRequestDto = new UserRequestDto();
        userRequestDto.setUserId(1L);
        userRequestDto.setUserName("Test User");
        userRequestDto.setEmail("testuser@example.com");
        userRequestDto.setStatus("ACTIVE");
        userRequestDto.setRoleIds(Arrays.asList(1L, 2L));
        userRequestDto.setGroupIds(Arrays.asList(1L, 2L));
        userRequestDto.setUpdatedBy("Admin");

        user = new User();
        user.setUserId(1L);
        user.setUserName("Test User");
        user.setEmail("testuser@example.com");
        user.setStatus(Status.ACTIVE);
        user.setCreatedBy("Admin");

        role1 = new Role();
        role1.setRoleId(1L);
        role1.setRoleName("Admin");
        role1.setStatus(Status.ACTIVE);

        role2 = new Role();
        role2.setRoleId(2L);
        role2.setRoleName("User");
        role2.setStatus(Status.ACTIVE);

        group1 = new Groups();
        group1.setGroupId(1L);
        group1.setGroupName("Group 1");
        group1.setStatus(Status.ACTIVE);

        group2 = new Groups();
        group2.setGroupId(2L);
        group2.setGroupName("Group 2");
        group2.setStatus(Status.ACTIVE);
    }

    @Test
    public void testCreateUser_Success() {
        when(userRepository.findByUserName(anyString())).thenReturn(Optional.empty());
        when(userRoleRepository.findRoleNameByUserId(anyLong())).thenReturn(Collections.singletonList(new Object[]{1L, "Admin"}));
        when(userGroupRepository.findGroupNameByUserId(anyLong())).thenReturn(Collections.singletonList(new Object[]{1L, "Group 1"}));
        when(groupRoleRepository.findRoleNamesByGroupId(anyLong())).thenReturn(Collections.singletonList(new Object[]{1L, "Admin"}));
        when(userRepository.save(any(User.class))).thenReturn(user);

        ResponseEntity<UserResponseDto> response = userServiceImpl.saveOrUpdateUser(userRequestDto);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Test User", response.getBody().getUserName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testCreateUser_AlreadyExists() {
        when(userRepository.findByUserName(anyString())).thenReturn(Optional.of(user));

        assertThrows(UserAlreadyExistsException.class, () -> {
            userServiceImpl.saveOrUpdateUser(userRequestDto);
        });

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testUpdateUser_Success() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRoleRepository.findRoleNameByUserId(anyLong())).thenReturn(Collections.singletonList(new Object[]{1L, "Admin"}));
        when(userGroupRepository.findGroupNameByUserId(anyLong())).thenReturn(Collections.singletonList(new Object[]{1L, "Group 1"}));
        when(groupRoleRepository.findRoleNamesByGroupId(anyLong())).thenReturn(Collections.singletonList(new Object[]{1L, "Admin"}));
        when(userRepository.save(any(User.class))).thenReturn(user);

        ResponseEntity<UserResponseDto> response = userServiceImpl.saveOrUpdateUser(userRequestDto);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testUpdateUser_UserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userServiceImpl.saveOrUpdateUser(userRequestDto);
        });

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testGetUserById_Success() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRoleRepository.findRoleNameByUserId(anyLong())).thenReturn(Collections.singletonList(new Object[]{1L, "Admin"}));
        when(userGroupRepository.findGroupNameByUserId(anyLong())).thenReturn(Collections.singletonList(new Object[]{1L, "Group 1"}));
        when(groupRoleRepository.findRoleNamesByGroupId(anyLong())).thenReturn(Collections.singletonList(new Object[]{1L, "Admin"}));

        ResponseEntity<UserResponseDto> response = userServiceImpl.getUserById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Test User", response.getBody().getUserName());
    }

    @Test
    public void testGetUserById_UserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userServiceImpl.getUserById(1L);
        });
    }

    @Test
    public void testGetUsers_Success() {
        Page<User> page = new PageImpl<>(Collections.singletonList(user));
        when(userRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
        when(userRoleRepository.findRoleNameByUserId(anyLong())).thenReturn(Collections.singletonList(new Object[]{1L, "Admin"}));
        when(userGroupRepository.findGroupNameByUserId(anyLong())).thenReturn(Collections.singletonList(new Object[]{1L, "Group 1"}));
        when(groupRoleRepository.findRoleNamesByGroupId(anyLong())).thenReturn(Collections.singletonList(new Object[]{1L, "Admin"}));

        ResponseEntity<PaginationResponseDto<UserResponseDto>> response = userServiceImpl.getUsers(1L, "Test User", "testuser@example.com", "ACTIVE", Pageable.unpaged());

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getResponses().size());
    }

    @Test
    public void testGetAllRoles_Success() {
        when(roleRepository.findByStatus(Status.ACTIVE)).thenReturn(Arrays.asList(role1, role2));

        ResponseEntity<List<RoleInfoDto>> response = userServiceImpl.getAllRoles();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals("Admin", response.getBody().get(0).getRoleName());
        assertEquals("User", response.getBody().get(1).getRoleName());
    }

    @Test
    public void testGetAllRoles_Empty() {
        when(roleRepository.findByStatus(Status.ACTIVE)).thenReturn(Collections.emptyList());

        ResponseEntity<List<RoleInfoDto>> response = userServiceImpl.getAllRoles();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().size());
    }
    @Test
    public void testGetAllGroups_Success() {
        when(groupRepository.findByStatus(Status.ACTIVE)).thenReturn(Arrays.asList(group1, group2));
        when(groupRoleRepository.findRoleNamesByGroupId(1L)).thenReturn(Collections.singletonList(new Object[]{1L, "Admin"}));
        when(groupRoleRepository.findRoleNamesByGroupId(2L)).thenReturn(Collections.singletonList(new Object[]{2L, "User"}));

        ResponseEntity<List<GroupNameDto>> response = userServiceImpl.getAllGroups();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals("Group 1", response.getBody().get(0).getGroupName());
        assertEquals("Group 2", response.getBody().get(1).getGroupName());
        assertEquals("Admin", response.getBody().get(0).getRoles().get(0).getRoleName());
        assertEquals("User", response.getBody().get(1).getRoles().get(0).getRoleName());
    }

    @Test
    public void testGetAllGroups_Empty() {
        when(groupRepository.findByStatus(Status.ACTIVE)).thenReturn(Collections.emptyList());

        ResponseEntity<List<GroupNameDto>> response = userServiceImpl.getAllGroups();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().size());
    }
}
