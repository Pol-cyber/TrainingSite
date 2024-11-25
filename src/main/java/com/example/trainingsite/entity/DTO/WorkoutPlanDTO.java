package com.example.trainingsite.entity.DTO;

import com.example.trainingsite.entity.WorkoutPlan;
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
