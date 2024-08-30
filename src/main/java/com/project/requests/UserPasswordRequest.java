package com.project.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserPasswordRequest {
    private String username;

    @NotBlank(message = "Password can not be black")
    private String password;

    @NotBlank(message = "New password can not be blank")
    private String newPassword;

    @NotBlank(message = "Retype password can not be blank")
    private String retypePassword;

}
