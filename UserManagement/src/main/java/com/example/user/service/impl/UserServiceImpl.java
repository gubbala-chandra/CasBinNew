package com.example.user.service.impl;

import com.example.user.dto.*;
import com.example.user.entity.*;
import com.example.user.enums.Status;
import com.example.user.exception.UserAlreadyExistsException;
import com.example.user.exception.UserNotFoundException;
import com.example.user.repository.*;
import com.example.user.service.UserService;
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

import static com.example.user.util.Utils.isEmpty;
import static com.example.user.util.Utils.formatSafe;
import static com.example.user.specification.UserSpecification.getRoleSpecification;

@Service
public class UserServiceImpl implements UserService {

    Logger log = LogManager.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserGroupRepository userGroupRepository;

    @Autowired
    private GroupRoleRepository groupRoleRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Override
    public ResponseEntity<UserResponseDto> saveOrUpdateUser(UserRequestDto userRequestDto) {
        if(!isEmpty(userRequestDto.getUserId()) && userRequestDto.getUserId() != 0) {
            return updateUser(userRequestDto);
        } else {
            return createUser(userRequestDto);
        }
    }

    private ResponseEntity<UserResponseDto> createUser(UserRequestDto userRequestDto) {
        Optional<User> userExists =  userRepository.findByUserName(userRequestDto.getUserName());
        if(userExists.isPresent()) {
            throw new UserAlreadyExistsException(formatSafe("%s already exists", userRequestDto.getUserName()));
        }
        User user = new User();
        user.setUserName(userRequestDto.getUserName());
        user.setEmail(userRequestDto.getEmail());
        user.setStatus(!isEmpty(userRequestDto.getStatus()) ? Status.valueOf(userRequestDto.getStatus()) : Status.ACTIVE);
        user.setCreatedBy(userRequestDto.getUpdatedBy());

        for(Long roleId : userRequestDto.getRoleIds()) {
            UserRole role = new UserRole();
            role.setRoleId(roleId);
            user.addUserRole(role);
        }
        for(Long groupId : userRequestDto.getGroupIds()) {
            UserGroup group = new UserGroup();
            group.setGroupId(groupId);
            user.addUserGroup(group);
        }
        userRepository.save(user);
        List<RoleInfoDto> rolesList = getRolesListFromUserRole(user.getUserId());
        List<GroupNameDto> groupList = getGroupListFromUserGroup(user.getUserId());
        List<RoleInfoDto> groupRoleList = groupList.stream().flatMap(gl -> gl.getRoles().stream()).collect(Collectors.toList());
        rolesList.addAll(groupRoleList);

        return ResponseEntity.status(HttpStatus.CREATED).body(buildResponse(user, rolesList, groupList));
    }

    private ResponseEntity<UserResponseDto> updateUser(UserRequestDto userRequestDto) {
        Optional<User> userExists =  userRepository.findByUserName(userRequestDto.getUserName());
        if(userExists.isPresent() && userExists.get().getUserId() != userRequestDto.getUserId()) {
            throw new UserAlreadyExistsException(formatSafe("%s already exists for another userId: %s", userRequestDto.getUserName(), userExists.get().getUserId()));
        }
        User user = userRepository.findById(userRequestDto.getUserId()).get();
        if(!isEmpty(userRequestDto.getUserName())) {
            user.setUserName(userRequestDto.getUserName());
        }
        if(!isEmpty(userRequestDto.getEmail())) {
            user.setEmail(userRequestDto.getEmail());
        }
        if(!isEmpty(userRequestDto.getStatus())) {
            user.setStatus(Status.valueOf(userRequestDto.getStatus()));
        }
        user.setUpdatedBy(userRequestDto.getUpdatedBy());
        user.getUserRoles().clear();
        user.getUserGroups().clear();

        for(Long roleId : userRequestDto.getRoleIds()) {
            UserRole role = new UserRole();
            role.setRoleId(roleId);
            user.addUserRole(role);
        }
        for(Long groupId : userRequestDto.getGroupIds()) {
            UserGroup group = new UserGroup();
            group.setGroupId(groupId);
            user.addUserGroup(group);
        }
        userRepository.save(user);
        List<RoleInfoDto> rolesList = getRolesListFromUserRole(userRequestDto.getUserId());
        List<GroupNameDto> groupList = getGroupListFromUserGroup(userRequestDto.getUserId());
        List<RoleInfoDto> groupRoleList = groupList.stream().flatMap(gl -> gl.getRoles().stream()).collect(Collectors.toList());
        rolesList.addAll(groupRoleList);

        return ResponseEntity.status(HttpStatus.OK).body(buildResponse(user, rolesList, groupList));
    }

    private List<RoleInfoDto> getRolesListFromUserRole(Long userId) {
        return userRoleRepository.findRoleNameByUserId(userId).stream()
                .map(obj -> new RoleInfoDto(Long.parseLong(obj[0].toString()), (String)obj[1], false)).collect(Collectors.toList());
    }

    private List<GroupNameDto> getGroupListFromUserGroup(Long userId) {
        return userGroupRepository.findGroupNameByUserId(userId).stream()
                .map(obj -> new GroupNameDto(Long.parseLong(obj[0].toString()), (String)obj[1],
                        (groupRoleRepository.findRoleNamesByGroupId(Long.parseLong(obj[0].toString())).stream()
                                .map(gr -> new RoleInfoDto(Long.parseLong(gr[0].toString()), (String)gr[1], true)).collect(Collectors.toList()))))
                .collect(Collectors.toList());
    }

    private UserResponseDto buildResponse(User user, List<RoleInfoDto> rolesList, List<GroupNameDto> groupList) {
        return UserResponseDto.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .email(user.getEmail())
                .status(user.getStatus().name())
                .roles(rolesList)
                .groups(groupList)
                .build();
    }
    @Override
    public ResponseEntity<PaginationResponseDto<UserResponseDto>> getUsers(Long userId, String userName, String email, String status, Pageable pageable) {
        Page<User> page = userRepository.findAll(getRoleSpecification(userId, userName, email, status), pageable);

        List<UserResponseDto> responses = page.getContent().stream().map(user -> {
            List<RoleInfoDto> rolesList = getRolesListFromUserRole(user.getUserId());
            List<GroupNameDto> groupList = getGroupListFromUserGroup(user.getUserId());
            List<RoleInfoDto> groupRoleList = groupList.stream().flatMap(gl -> gl.getRoles().stream()).collect(Collectors.toList());
            rolesList.addAll(groupRoleList);
            return buildResponse(user, rolesList, groupList);
        }).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(
                PaginationResponseDto.<UserResponseDto>builder()
                        .responses(responses)
                        .pageSize(page.getSize())
                        .totalItems(page.getTotalElements())
                        .pageNumber(page.getNumber())
                        .totalPages(page.getTotalPages())
                        .build()
        );
    }

    @Override
    public ResponseEntity<UserResponseDto> getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(formatSafe("User does not exist for user Id: %s", userId)));
        List<RoleInfoDto> rolesList = getRolesListFromUserRole(userId);
        List<GroupNameDto> groupList = getGroupListFromUserGroup(userId);
        List<RoleInfoDto> groupRoleList = groupList.stream().flatMap(gl -> gl.getRoles().stream()).collect(Collectors.toList());
        rolesList.addAll(groupRoleList);
        return ResponseEntity.status(HttpStatus.OK).body(buildResponse(user, rolesList, groupList));
    }

    @Override
    public ResponseEntity<List<RoleInfoDto>> getAllRoles() {
        List<Role> roles = roleRepository.findByStatus(Status.ACTIVE);
        List<RoleInfoDto> response = roles.stream().map(r -> new RoleInfoDto(r.getRoleId(), r.getRoleName(), false)).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<List<GroupNameDto>> getAllGroups() {
        List<Group> groups = groupRepository.findByStatus(Status.ACTIVE);
        List<GroupNameDto> response = groups.stream().map(g -> new GroupNameDto(g.getGroupId(), g.getGroupName(),
                (groupRoleRepository.findRoleNamesByGroupId(g.getGroupId())
                        .stream().map(obj -> new RoleInfoDto(Long.parseLong(obj[0].toString()), (String)obj[1], true)).collect(Collectors.toList()))
        )).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
