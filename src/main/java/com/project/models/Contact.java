package com.project.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Getter
@Setter
@Entity
@Table(name = "contact")
public class Contact extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "vocative", nullable = false)
    private String vocative;

    @Column(name = "phonenumber", nullable = false, length = 15)
    private String phoneNumber;

    @Column(name = "gmail", nullable = false)
    private String gmail;

    @Column(name = "note", nullable = false)
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User contactUserEntities;
}
