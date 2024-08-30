package com.project.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "dailybooking")
public class DailyBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "time_id", nullable = false)
    private Long timeId;

    @Column(name = "count", nullable = false)
    private Integer count;
}
