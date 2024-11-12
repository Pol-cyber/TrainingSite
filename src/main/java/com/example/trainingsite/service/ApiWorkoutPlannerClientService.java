package com.example.trainingsite.service;

import com.example.trainingsite.Entity.DTO.Schedule;
import com.example.trainingsite.Entity.DTO.WorkoutPlanDTO;
import com.example.trainingsite.Entity.UserCharacteristic;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@Service
public class ApiWorkoutPlannerClientService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Value("${api.workout.url}")
    private String apiUrl;
    @Value("${api.workout.key}")
    private String apiKey;
    @Value("${api.workout.host}")
    private String apiHost;

    public ApiWorkoutPlannerClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String fetchWorkoutPlan(WorkoutPlanDTO workPlanData, UserCharacteristic userCharacteristic) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-rapidapi-key", apiKey);
        headers.set("x-rapidapi-host", apiHost);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("goal", workPlanData.getGoal());
        requestBody.put("fitness_level", workPlanData.getFitnessLevel());
        requestBody.put("preferences", workPlanData.getTrainingTypes());
        List<String> health_conditions = new ArrayList<>(Arrays.asList(userCharacteristic.getGender(),
                userCharacteristic.getHeight().toString(),userCharacteristic.getWeight().toString()));
        health_conditions.addAll(userCharacteristic.getHealthConditions());
        requestBody.put("health_conditions", health_conditions);
        Schedule schedule = new Schedule(workPlanData.getDaysPerWeek(),workPlanData.getSessionDuration());
        requestBody.put("schedule", schedule);
        requestBody.put("plan_duration_weeks", workPlanData.getTotalWeeks());
        requestBody.put("lang", "en");

        String json = objectMapper.writeValueAsString(requestBody);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);

        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);

        return response.getBody();
    }
}
