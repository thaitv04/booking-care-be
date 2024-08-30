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
public class BookingDTO extends BaseDTO {
    @NotBlank(message = "Fullname can not be blank")
    private String fullname;

    @NotBlank(message = "Address can not be blank")
    private String address;

    @NotBlank(message = "Gmail can not be blank")
    private String gmail;

    @NotBlank(message = "Phonenumber can not be blank")
    private String phoneNumber;

    private String dateOfBirth;

    private String dateBooking;

    private Long doctorId;

    private Long majorId;

    private Long timeBookingId;

    private String note;
}
