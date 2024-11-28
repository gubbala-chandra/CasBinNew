package com.example.user.service.impl;

import com.example.user.dto.GroupRequestDto;
import com.example.user.dto.GroupResponseDto;
import com.example.user.dto.PaginationResponseDto;
import com.example.user.dto.RoleIdNameDto;
import com.example.user.entity.Groups;
import com.example.user.entity.GroupRole;
import com.example.user.enums.Status;
import com.example.user.exception.GroupAlreadyExistsException;
import com.example.user.exception.GroupNotFoundException;
import com.example.user.repository.GroupRepository;
import com.example.user.repository.GroupRoleRepository;
import com.example.user.service.GroupService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.user.util.Utils.formatSafe;
import static com.example.user.util.Utils.isEmpty;
import static com.example.user.specification.GroupSpecification.getGroupSpecification;

@Service
public class GroupServiceImpl implements GroupService {

    Logger log = LogManager.getLogger(GroupServiceImpl.class);

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupRoleRepository groupRoleRepository;

    @Override
    public ResponseEntity<GroupResponseDto> saveOrUpdateGroup(GroupRequestDto groupRequestDto) {
        if(!isEmpty(groupRequestDto.getGroupId()) && groupRequestDto.getGroupId() != 0) {
            return updateGroup(groupRequestDto);
        } else {
            return createGroup(groupRequestDto);
        }
    }

    private ResponseEntity<GroupResponseDto> createGroup(GroupRequestDto groupRequestDto) {
        Optional<Groups> groupExists = groupRepository.findByGroupName(groupRequestDto.getGroupName());
        if(groupExists.isPresent()) {
            throw new GroupAlreadyExistsException(formatSafe("Group already exists with given groupName: %s", groupRequestDto.getGroupName()));
        }
        Groups group = new Groups();
        group.setGroupName(groupRequestDto.getGroupName());
        group.setDescription(groupRequestDto.getDescription());
        group.setStatus(Status.valueOf(groupRequestDto.getStatus()));
        group.setCreatedBy(groupRequestDto.getUpdatedBy());

        for (Long roleId : groupRequestDto.getRoleIds()) {
            GroupRole groupRole = new GroupRole();
            groupRole.setRoleId(roleId);
            group.addGroupRole(groupRole);
        }
        groupRepository.save(group);

        List<RoleIdNameDto> roles = Optional.of(groupRoleRepository.findRoleNamesByGroupId(group.getGroupId()))
                .orElseThrow(() -> new GroupNotFoundException("Group not found with the specified criteria"))
                .stream().map(obj -> new RoleIdNameDto(Long.parseLong(obj[0].toString()), (String)obj[1])).collect(Collectors.toList());
        GroupResponseDto response = GroupResponseDto.builder()
                .groupId(group.getGroupId())
                .groupName(group.getGroupName())
                .description(group.getDescription())
                .status(group.getStatus().name())
                .roles(roles)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    private ResponseEntity<GroupResponseDto> updateGroup(GroupRequestDto groupRequestDto) {
        Optional<Groups> groupExists = groupRepository.findByGroupName(groupRequestDto.getGroupName());
        if(groupExists.isPresent() && groupExists.get().getGroupId() != groupRequestDto.getGroupId()) {
            throw new GroupAlreadyExistsException(formatSafe("Group already exists with given groupName: %s", groupRequestDto.getGroupName()));
        }
        Groups group = groupRepository.findById(groupRequestDto.getGroupId())
                .orElseThrow(() -> new GroupNotFoundException(formatSafe("Group does not exist for group Id: %s", groupRequestDto.getGroupId())));

        group.getGroupRoles().clear();

        for (Long roleId : groupRequestDto.getRoleIds()) {
            if (group.getGroupRoles().stream().noneMatch(r -> r.getRoleId().equals(roleId))) {
                GroupRole newGroupRole = new GroupRole();
                newGroupRole.setRoleId(roleId);
                group.addGroupRole(newGroupRole);
            }
        }
        groupRepository.save(group);
        List<RoleIdNameDto> roles = Optional.of(groupRoleRepository.findRoleNamesByGroupId(group.getGroupId()))
                .orElseThrow(() -> new GroupNotFoundException("Group not found with the specified criteria"))
                .stream().map(obj -> new RoleIdNameDto(Long.parseLong(obj[0].toString()), (String)obj[1])).collect(Collectors.toList());;
        GroupResponseDto response = GroupResponseDto.builder()
                .groupId(group.getGroupId())
                .groupName(group.getGroupName())
                .description(group.getDescription())
                .status(group.getStatus().name())
                .roles(roles)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<PaginationResponseDto<GroupResponseDto>> getGroupWithRoles(Long groupId, String groupName, String description, String status, Pageable pageable) {
        Page<Groups> groupPage = groupRepository.findAll(getGroupSpecification(groupId, groupName, description, status), pageable);
        List<GroupResponseDto> groupResponseList = groupPage.getContent().stream()
                .map(group -> {
                    List<RoleIdNameDto> roles = Optional.of(groupRoleRepository.findRoleNamesByGroupId(group.getGroupId()))
                            .orElseThrow(() -> new GroupNotFoundException("Group not found with the specified criteria"))
                            .stream().map(obj -> new RoleIdNameDto(Long.parseLong(obj[0].toString()), (String)obj[1])).collect(Collectors.toList());;
                    GroupResponseDto response = GroupResponseDto.builder()
                            .groupId(group.getGroupId())
                            .groupName(group.getGroupName())
                            .description(group.getDescription())
                            .status(group.getStatus().name())
                            .roles(roles)
                            .build();
                    return response;
                })
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(
                PaginationResponseDto.<GroupResponseDto>builder()
                        .responses(groupResponseList)
                        .pageSize(groupPage.getSize())
                        .totalItems(groupPage.getTotalElements())
                        .pageNumber(groupPage.getNumber())
                        .totalPages(groupPage.getTotalPages())
                    .build()
        );
    }

    @Override
    public ResponseEntity<GroupResponseDto> getGroupById(Long groupId) {
        Groups group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(formatSafe("Group does not exist for group Id: %s", groupId)));
        List<RoleIdNameDto> roles = Optional.of(groupRoleRepository.findRoleNamesByGroupId(group.getGroupId()))
                .orElseThrow(() -> new GroupNotFoundException("Group not found with the specified criteria"))
                .stream().map(obj -> new RoleIdNameDto(Long.parseLong(obj[0].toString()), (String)obj[1])).collect(Collectors.toList());
        GroupResponseDto response = GroupResponseDto.builder()
                .groupId(group.getGroupId())
                .groupName(group.getGroupName())
                .description(group.getDescription())
                .status(group.getStatus().name())
                .roles(roles)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
