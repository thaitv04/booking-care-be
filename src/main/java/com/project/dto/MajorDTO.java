package com.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MajorDTO extends BaseDTO {
    @NotBlank(message = "Name can not be blank")
    private String name;

    @NotBlank(message = "Description can not be blank")
    private String description;

    @NotBlank(message = "Short description can not be blank")
    private String shortDescription;
}
