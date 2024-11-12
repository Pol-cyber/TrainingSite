package com.example.trainingsite.service;

import com.example.trainingsite.Entity.DTO.WorkoutPlanDTO;
import com.example.trainingsite.Entity.Exercise;
import com.example.trainingsite.Entity.WorkoutDay;
import com.example.trainingsite.Entity.WorkoutPlan;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Service
public class WorkoutMapper {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final TranslateService translateService;

    public WorkoutMapper(TranslateService translateService) {
        this.translateService = translateService;
    }

    public WorkoutPlan mapJsonToWorkoutPlan(String jsonResponse, WorkoutPlanDTO workoutPlanDTO) throws Exception {
        JsonNode resultNode = objectMapper.readTree(jsonResponse).get("result");

        WorkoutPlan workoutPlan = new WorkoutPlan();
        workoutPlan.setTotalWeeks(workoutPlanDTO.getTotalWeeks());
        workoutPlan.setDaysPerWeek(workoutPlanDTO.getDaysPerWeek());
        workoutPlan.setSessionDuration(workoutPlanDTO.getSessionDuration());

        // Перелік елементів для перекладу
        List<String> informationToTranslate = List.of(
                workoutPlanDTO.getGoal(),
                workoutPlanDTO.getFitnessLevel(),
                String.join(", ", workoutPlanDTO.getTrainingTypes()), // Приєднання без використання `toString`
                resultNode.get("seo_title").asText(),
                resultNode.get("seo_content").asText()
        );

        List<String> translatedInformation = translateService.translate(informationToTranslate, "en", "uk");

        // Призначення перекладених даних
        workoutPlan.setGoal(translatedInformation.get(0));
        workoutPlan.setFitnessLevel(translatedInformation.get(1));
        workoutPlan.setTrainingTypes(List.of(translatedInformation.get(2).split(", ")));
        workoutPlan.setSeo_title(translatedInformation.get(3));
        workoutPlan.setSeo_content(translatedInformation.get(4));

        List<WorkoutDay> workoutDays = new ArrayList<>();
        for (JsonNode dayNode : resultNode.get("exercises")) {
            WorkoutDay workoutDay = new WorkoutDay();
            workoutDay.setWorkoutPlan(workoutPlan);
            workoutDay.setDay(dayNode.get("day").asText());
            List<Exercise> exercises = new ArrayList<>();
            List<String> informationAboutExercise = new ArrayList<>();
            for (JsonNode exerciseNode : dayNode.get("exercises")) {
                informationAboutExercise.add(exerciseNode.get("name").asText());
                informationAboutExercise.add(exerciseNode.get("duration").asText());
                informationAboutExercise.add(exerciseNode.get("repetitions").asText());
                informationAboutExercise.add(exerciseNode.get("sets").asText());
                informationAboutExercise.add(exerciseNode.get("equipment").asText());
            }
            List<String> translateInformationAboutExercise = translateService.translate(informationAboutExercise,"en","uk");
            Iterator<String> stringIterator = translateInformationAboutExercise.iterator();
            while (stringIterator.hasNext()) {
                Exercise exercise = new Exercise();
                exercise.setName(stringIterator.next());
                exercise.setDuration(stringIterator.next());
                exercise.setRepetitions(stringIterator.next());
                exercise.setSets(stringIterator.next());
                exercise.setEquipment(stringIterator.next());
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

