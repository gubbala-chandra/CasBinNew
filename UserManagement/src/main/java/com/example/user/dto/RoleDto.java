package com.example.user.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleDto {
    private Long roleId;
    private String roleName;
    private String description;
    private String status;
    private String updatedBy;
}
