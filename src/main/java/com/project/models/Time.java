package com.project.models;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "time")
public class Time extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start", nullable = false)
    private Integer start;

    @Column(name = "end", nullable = false)
    private Integer end;

    @OneToMany(mappedBy = "timeBookingEntities", fetch = FetchType.LAZY)
    private List<Booking> bookingEntities = new ArrayList<>();
}
