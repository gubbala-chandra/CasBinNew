package com.example.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PaginationResponseDto<T> implements Serializable {

    private List<T> responses;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalItems;
    private Integer totalPages;
}
