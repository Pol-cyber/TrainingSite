package com.example.trainingsite.Entity.DTO;

import com.example.trainingsite.Entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class WorkoutPlanDTO {

    private String goal;
    private String fitnessLevel;
    private Integer totalWeeks;
    private Integer daysPerWeek;
    private Integer sessionDuration;
    private List<String> trainingTypes;

}
