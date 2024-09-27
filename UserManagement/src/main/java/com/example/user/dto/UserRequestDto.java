package com.example.user.dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDto {

    private Long userId;
    private String userName;
    private String email;
    private String updatedBy;
    private String status;
    private List<Long> groupIds;
    private List<Long> roleIds;
}
