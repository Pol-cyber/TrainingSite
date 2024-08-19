package com.example.trainingsite.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class MessageGeneralChat {

    @Id
    @GeneratedValue
    private int id;


    private String senderUsername;

    private String text;
}
