package com.example.user.dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupRequestDto {
    private Long groupId;
    private String groupName;
    private String description;
    private String status;
    private String updatedBy;
    private List<Long> roleIds;
}
