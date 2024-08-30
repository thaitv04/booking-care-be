package com.project.responses;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MajorResponse {
    private Long id;
    private String name;
    private String shortDescription;
    private String description;
    private String image;
    private Integer numberOfDoctor;
}
