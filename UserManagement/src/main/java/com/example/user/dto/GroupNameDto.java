package com.example.user.dto;


import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupNameDto {
    private Long groupId;
    private String groupName;
    private List<RoleInfoDto> roles;
}
