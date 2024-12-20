package com.example.trainingsite.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.*;


@Data
@Entity
@NoArgsConstructor
@Table(name = "userAcc")
public class User implements UserDetails, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(length = 20)
    private String username;

    @JsonIgnore
    private String password;

    private String email;

    private byte[] image;

    private String role;

    private boolean newsletterSub;

    @OneToMany(mappedBy = "receiverId")
    @SQLRestriction("um1_0.is_read = 'false'")
    private List<MessageUserToUser> unreadMessage;


    @Enumerated(EnumType.STRING)
    private Status status = Status.OFFLINE;

    @OneToOne(mappedBy = "user")
    private UserCharacteristic userCharacteristic;

    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER)
    private List<WorkoutPlan> workoutPlans = new ArrayList<>();

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                '}';
    }

    public enum Status {
        ONLINE,OFFLINE
    }

    public User(String username, String password, String email, byte[] image, String role,boolean newsletterSub) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.image = image;
        this.role = role;
        this.newsletterSub = newsletterSub;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority(role));
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
