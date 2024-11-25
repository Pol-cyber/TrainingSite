package com.example.trainingsite.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;


@Entity
@Data
public class News {

    @Id
    @GeneratedValue
    private int id;

    private byte[] image;
    @Column(nullable = false,length = 80)
    private String title;

    @Column(length = 750,nullable = false)
    private String text;
    private String link;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date data;

    private boolean actual = true;

}


