package com.example.user.dto;


import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleInfoDto extends RoleIdNameDto {
    private Boolean disabled;

    public RoleInfoDto(Long roleId, String roleName, Boolean disabled) {
        super(roleId, roleName);
        this.disabled=disabled;
    }
}
