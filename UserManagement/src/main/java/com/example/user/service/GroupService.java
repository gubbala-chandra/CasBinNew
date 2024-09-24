package com.example.user.service;

import com.example.user.dto.GroupRequestDto;
import com.example.user.dto.GroupResponseDto;
import com.example.user.dto.PaginationResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface GroupService {

    ResponseEntity<GroupResponseDto> saveOrUpdateGroup(GroupRequestDto groupRequestDto);
    ResponseEntity<PaginationResponseDto<GroupResponseDto>> getGroupWithRoles(Long groupId, String groupName, String description, String status, Pageable pageable);
    ResponseEntity<GroupResponseDto> getGroupById(Long groupdId);
}
