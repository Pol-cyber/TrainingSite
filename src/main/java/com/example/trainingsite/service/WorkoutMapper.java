package com.example.trainingsite.service;

import com.example.trainingsite.Entity.Exercise;
import com.example.trainingsite.Entity.WorkoutDay;
import com.example.trainingsite.Entity.WorkoutPlan;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WorkoutMapper {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public WorkoutPlan mapJsonToWorkoutPlan(String jsonResponse,List<String> trainingTypes) throws Exception {
        JsonNode resultNode = objectMapper.readTree(jsonResponse).get("result");

        WorkoutPlan workoutPlan = new WorkoutPlan();
        workoutPlan.setGoal(resultNode.get("goal").asText());
        workoutPlan.setFitnessLevel(resultNode.get("fitness_level").asText());
        workoutPlan.setTotalWeeks(resultNode.get("total_weeks").asInt());
        workoutPlan.setDaysPerWeek(resultNode.get("schedule").get("days_per_week").asInt());
        workoutPlan.setSessionDuration(resultNode.get("schedule").get("session_duration").asInt());
        workoutPlan.setTrainingTypes(trainingTypes);
        workoutPlan.setSeo_title(resultNode.get("seo_title").asText());
        workoutPlan.setSeo_content(resultNode.get("seo_content").asText());

        List<WorkoutDay> workoutDays = new ArrayList<>();
        for (JsonNode dayNode : resultNode.get("exercises")) {
            WorkoutDay workoutDay = new WorkoutDay();
            workoutDay.setWorkoutPlan(workoutPlan);
            workoutDay.setDay(dayNode.get("day").asText());
            List<Exercise> exercises = new ArrayList<>();
            for (JsonNode exerciseNode : dayNode.get("exercises")) {
                Exercise exercise = new Exercise();
                exercise.setName(exerciseNode.get("name").asText());
                exercise.setDuration(exerciseNode.get("duration").asText());
                exercise.setRepetitions(exerciseNode.get("repetitions").asText());
                exercise.setSets(exerciseNode.get("sets").asText());
                exercise.setEquipment(exerciseNode.get("equipment").asText());
                exercise.setWorkoutDay(workoutDay);
                exercises.add(exercise);
            }
            workoutDay.setExercises(exercises);
            workoutDays.add(workoutDay);
        }
        workoutPlan.setWorkoutDays(workoutDays);
        return workoutPlan;
    }
}

