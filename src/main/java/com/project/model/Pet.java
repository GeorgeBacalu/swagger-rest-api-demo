package com.project.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class Pet {
    private Long id;

    private Category category;

    @NotNull
    @ApiModelProperty(name = "name", dataType = "string", example = "doggie", required = true)
    private String name;

    @NotNull
    @ApiModelProperty(name = "photoUrls", dataType = "array", required = true)
    private List<String> photoUrls;

    private List<Tag> tags;

    @ApiModelProperty(value = "pet status in the store")
    private Status status;
}
