package com.project.models;

import com.project.constants.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "booking")
public class Booking extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fullname", nullable = false, length = 50)
    private String fullname;

    @Column(name = "address", nullable = false, length = 255)
    private String address;

    @Column(name = "dateofbirth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "gmail", nullable = false)
    private String gmail;

    @Column(name = "phonenumber", nullable = false)
    private String phoneNumber;

    @Column(name = "note", nullable = false)
    private String note;

    @Column(name = "major_id")
    private Long majorId;

    @Column(name = "datebooking", nullable = false)
    private LocalDate dateBooking;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @Column(name = "token")
    private String token;

    @Column(name = "denyreason")
    private String denyReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User userBookingEntities;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "time_id")
    private Time timeBookingEntities;

}
