package com.example.trainingsite.entity.DTO;

import lombok.Data;

import java.util.List;

@Data

public class CharacteristicDTO {
    private Double weight;
    private Double height;
    private String gender;
    private String trainingGoal;
    private String fitnessLevel;
    private List<String> healthConditions;
}
