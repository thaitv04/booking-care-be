package com.project.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchRequest {
    private String name;
    private String address;
    private String gmail;
    private String phoneNumber;
    private String dateOfBirthFrom;
    private String dateOfBirthTo;
    private String gender;
    private String ethnicity;
    private Integer leaveFrom;
    private Integer leaveTo;
    private Integer experienceFrom;
    private Integer experienceTo;
    private Integer certificationFrom;
    private Integer certificationTo;
    private String degree;
    private Long major;
    private Long status;
}
