package com.example.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {
    private Long roleId;
    private String roleName;
    private String description;
    private String status;
    private String updatedBy;
}
