package com.example.user.service;

import com.example.user.dto.GroupRequestDto;
import com.example.user.dto.GroupResponseDto;
import com.example.user.dto.PaginationResponseDto;
import com.example.user.entity.Groups;
import com.example.user.entity.GroupRole;
import com.example.user.enums.Status;
import com.example.user.exception.GroupAlreadyExistsException;
import com.example.user.exception.GroupNotFoundException;
import com.example.user.repository.GroupRepository;
import com.example.user.repository.GroupRoleRepository;
import com.example.user.service.impl.GroupServiceImpl;
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

public class GroupServiceImplTest {

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private GroupRoleRepository groupRoleRepository;

    @InjectMocks
    private GroupServiceImpl groupServiceImpl;

    private GroupRequestDto groupRequestDto;
    private Groups group;
    private GroupRole groupRole;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        groupRequestDto = new GroupRequestDto();
        groupRequestDto.setGroupId(1L);
        groupRequestDto.setGroupName("Test Group");
        groupRequestDto.setDescription("Test Description");
        groupRequestDto.setStatus("ACTIVE");
        groupRequestDto.setRoleIds(Arrays.asList(1L, 2L));
        groupRequestDto.setUpdatedBy("Admin");

        group = new Groups();
        group.setGroupId(1L);
        group.setGroupName("Test Group");
        group.setDescription("Test Description");
        group.setStatus(Status.ACTIVE);
        group.setCreatedBy("Admin");

        groupRole = new GroupRole();
        groupRole.setRoleId(1L);
        group.addGroupRole(groupRole);
    }

    @Test
    public void testCreateGroup_Success() {
        when(groupRepository.findByGroupName(anyString())).thenReturn(Optional.empty());
        when(groupRoleRepository.findRoleNamesByGroupId(anyLong())).thenReturn(Collections.singletonList(new Object[]{1L, "Admin"}));
        when(groupRepository.save(any(Groups.class))).thenReturn(group);

        ResponseEntity<GroupResponseDto> response = groupServiceImpl.saveOrUpdateGroup(groupRequestDto);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Test Group", response.getBody().getGroupName());
        verify(groupRepository, times(1)).save(any(Groups.class));
    }

    @Test
    public void testCreateGroup_AlreadyExists() {
        when(groupRepository.findByGroupName(anyString())).thenReturn(Optional.of(group));

        assertThrows(GroupAlreadyExistsException.class, () -> {
            groupServiceImpl.saveOrUpdateGroup(groupRequestDto);
        });

        verify(groupRepository, never()).save(any(Groups.class));
    }

    @Test
    public void testUpdateGroup_Success() {
        groupRequestDto.setGroupId(1L);
        when(groupRepository.findByGroupName(anyString())).thenReturn(Optional.of(group));
        when(groupRepository.findById(anyLong())).thenReturn(Optional.of(group));
        when(groupRoleRepository.findRoleNamesByGroupId(anyLong())).thenReturn(Collections.singletonList(new Object[]{1L, "Admin"}));

        ResponseEntity<GroupResponseDto> response = groupServiceImpl.saveOrUpdateGroup(groupRequestDto);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(groupRepository, times(1)).save(any(Groups.class));
    }

    @Test
    public void testUpdateGroup_GroupNotFound() {
        when(groupRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(GroupNotFoundException.class, () -> {
            groupServiceImpl.saveOrUpdateGroup(groupRequestDto);
        });

        verify(groupRepository, never()).save(any(Groups.class));
    }

    @Test
    public void testGetGroupById_Success() {
        when(groupRepository.findById(anyLong())).thenReturn(Optional.of(group));
        when(groupRoleRepository.findRoleNamesByGroupId(anyLong())).thenReturn(Collections.singletonList(new Object[]{1L, "Admin"}));

        ResponseEntity<GroupResponseDto> response = groupServiceImpl.getGroupById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Test Group", response.getBody().getGroupName());
    }

    @Test
    public void testGetGroupById_GroupNotFound() {
        when(groupRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(GroupNotFoundException.class, () -> {
            groupServiceImpl.getGroupById(1L);
        });
    }

    @Test
    public void testGetGroupWithRoles_Success() {
        Page<Groups> groupPage = new PageImpl<>(Collections.singletonList(group));
        when(groupRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(groupPage);
        when(groupRoleRepository.findRoleNamesByGroupId(anyLong())).thenReturn(Collections.singletonList(new Object[]{1L, "Admin"}));

        ResponseEntity<PaginationResponseDto<GroupResponseDto>> response = groupServiceImpl.getGroupWithRoles(1L, "Test Group", "Test Description", "ACTIVE", Pageable.unpaged());

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getResponses().size());
    }
}
