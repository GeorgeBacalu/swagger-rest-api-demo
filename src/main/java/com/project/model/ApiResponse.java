package com.project.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse {
    private Integer code;

    private String type;

    private String message;
}
