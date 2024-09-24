package com.example.user.service.impl;

import com.example.user.dto.PaginationResponseDto;
import com.example.user.dto.RoleDto;
import com.example.user.entity.Role;
import com.example.user.enums.Status;
import com.example.user.exception.RoleAlreadyExistsException;
import com.example.user.exception.RoleNotPresetException;
import com.example.user.repository.RoleRepository;
import com.example.user.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Optional;

import static com.example.user.util.Utils.formatSafe;
import static com.example.user.util.Utils.isEmpty;
import static com.example.user.specification.RoleSpecification.getRoleSpecification;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public ResponseEntity<Role> saveOrUpdateRole(RoleDto roleDto) {
        if(roleDto.getRoleId() != null && roleDto.getRoleId() != 0) {
            return ResponseEntity.status(HttpStatus.OK).body(updateRole(roleDto));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(createRole(roleDto));
        }
    }

    private Role createRole(RoleDto roleDto) {
        Optional<Role> roleExists = roleRepository.findByRoleName(roleDto.getRoleName());
        if(roleExists.isPresent()) {
            throw new RoleAlreadyExistsException("RoleName already exists, cannot create role with same Name");
        }

        Role role = Role.builder()
                .roleName(roleDto.getRoleName())
                .description(roleDto.getDescription())
                .status(Status.ACTIVE)
                .createdBy(roleDto.getUpdatedBy())
                .build();
        return roleRepository.save(role);
    }

    private Role updateRole(RoleDto roleDto) {
        Role newRole = null;
        Optional<Role> roleExists = roleRepository.findByRoleName(roleDto.getRoleName());
        if(roleExists.isPresent() && roleExists.get().getRoleId() != roleDto.getRoleId()) {
            throw new RoleAlreadyExistsException(
                    formatSafe("RoleName already present with roleId: %s, " +
                            "trying to add same roleName to roleId: %s",
                            roleExists.get().getRoleId(),
                            roleDto.getRoleId()));
        }
        Optional<Role> role = roleRepository.findById(roleDto.getRoleId());
        if(role.isPresent()) {
            newRole = role.get();
            if(!isEmpty(roleDto.getRoleName())) {
                newRole.setRoleName(roleDto.getRoleName());
            }
            if(!isEmpty(roleDto.getDescription())) {
                newRole.setDescription(roleDto.getDescription());
            }
            if(!isEmpty(roleDto.getStatus()) && roleDto.getStatus().equalsIgnoreCase(Status.INACTIVE.name())) {
                newRole.setStatus(Status.INACTIVE);
            }
            newRole.setUpdatedBy(roleDto.getUpdatedBy());
            return roleRepository.save(newRole);
        } else {
            throw new RoleNotPresetException(formatSafe("No Role exists for roleId: %s", roleDto.getRoleId()));
        }
    }

    @Override
    public ResponseEntity<PaginationResponseDto<Role>> getRoles(Long roleId, String roleName, String description, String status, Pageable pageable) {
        Page<Role> pages = roleRepository.findAll(getRoleSpecification(roleId,roleName,description,status), pageable);
        return ResponseEntity.status(HttpStatus.OK).body(
                PaginationResponseDto.<Role>builder()
                        .responses(pages.getContent())
                        .pageSize(pages.getSize())
                        .totalItems(pages.getTotalElements())
                        .pageNumber(pages.getNumber())
                        .totalPages(pages.getTotalPages())
                        .build()
        );
    }

    @Override
    public ResponseEntity<Role> getRoleById(Long roleId) {
        Optional<Role> roleResponse = roleRepository.findById(roleId);
        if(roleResponse.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(roleResponse.get());
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}
