package com.example.trainingsite.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Data
public class MessageUserToUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sender_username",length = 20)
    private String senderId;

    @Column(name = "receiver_username",length = 20)
    private String receiverId;

    private String text;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date data;

    private boolean isRead;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "sender_username", insertable = false, updatable = false)
    private User sender;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "receiver_username", insertable = false, updatable = false)
    private User receiver;
}
