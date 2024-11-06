package com.example.trainingsite.Entity.DTO;

import com.example.trainingsite.Entity.User;
import com.example.trainingsite.Entity.WorkoutPlan;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class WorkoutPlanDTO {

    private String planName;
    private String goal;
    private String fitnessLevel;
    private Integer totalWeeks;
    private Integer daysPerWeek;
    private Integer sessionDuration;
    private List<String> trainingTypes;


    public WorkoutPlanDTO(WorkoutPlan workoutPlan) {
        planName = workoutPlan.getWorkoutPlanPK().getPlanName();
        goal = workoutPlan.getGoal();
        fitnessLevel = workoutPlan.getFitnessLevel();
        totalWeeks = workoutPlan.getTotalWeeks();
        daysPerWeek = workoutPlan.getDaysPerWeek();
        sessionDuration = workoutPlan.getSessionDuration();
        trainingTypes = workoutPlan.getTrainingTypes();
    }

}
