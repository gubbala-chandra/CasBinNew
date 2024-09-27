package com.example.user.dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {
    private Long userId;
    private String userName;
    private String email;
    private String status;
    private List<GroupNameDto> groups;
    private List<RoleInfoDto> roles;
}
