package com.example.user.dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupResponseDto {

    private Long groupId;
    private String groupName;
    private String description;
    private String status;
    private List<RoleIdNameDto> roles;
}
