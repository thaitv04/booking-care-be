package com.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO extends BaseDTO{
    @NotBlank(message = "Name can not be blank")
    private String name;

    @NotBlank(message = "Gmail can not be blank")
    private String gmail;

    @NotBlank(message = "PhoneNumber can not be blank")
    private String phoneNumber;

    @NotBlank(message = "Avatar can not be blank")
    private String avatar;

    @NotBlank(message = "Address can not be blank")
    private String address;

    @NotBlank(message = "Date of birth can not be blank")
    private String dateOfBirth;

    @NotBlank(message = "Username can not be blank")
    private String username;

    @NotBlank(message = "Identity can not be blank")
    private String identity;

    @NotBlank(message = "Ethnicity can not be blank")
    private String ethnicity;

    @NotBlank(message = "Gender can not be blank")
    private String gender;

    private String description;

    private Integer experience;

    private Integer certification;

    private String degree;

    @JsonProperty("major")
    private Long majorId;

    private String majorName;
}
