package com.project.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "gmail", nullable = false)
    private String gmail;

    @Column(name = "phonenumber", nullable = false, length = 15)
    private String phoneNumber;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "ethnicity", nullable = false, length = 30)
    private String ethnicity;

    @Column(name = "identity", nullable = false, length = 12)
    private String identity;

    @Column(name = "dateofbirth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "avatar", nullable = false)
    private String avatar;

    @Column(name = "idimage", length = 100)
    private String idimage;

    @Column(name = "description")
    private String description;

    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "experience")
    private Integer experience;

    @Column(name = "certification")
    private Integer certification;

    @Column(name = "degree", length = 10)
    private String degree;

    @Column(name = "deletedyear")
    private Integer deletedyear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role roleUserEntities;

    @OneToMany(mappedBy = "userBookingEntities", fetch = FetchType.LAZY)
    private List<Booking> bookingEntities = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "major_id")
    private Major majorUserEntities;

    @OneToMany(mappedBy = "contactUserEntities", fetch = FetchType.LAZY)
    private List<Contact> contactEntities = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority("ROLE_" + getRoleUserEntities().getName().toUpperCase()));
        return authorityList;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
