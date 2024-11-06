package com.example.trainingsite.service;

import com.example.trainingsite.Entity.DTO.WorkoutPlanDTO;
import com.example.trainingsite.Entity.User;
import com.example.trainingsite.Entity.WorkoutPlan;
import com.example.trainingsite.repository.WorkoutPlanRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class WorkoutPlanService {

    private final ApiWorkoutPlannerClientService apiWorkoutPlannerClientService;
    private final WorkoutMapper workoutMapper;
    private final WorkoutPlanRepo workoutPlanRepository;

    public WorkoutPlanService(ApiWorkoutPlannerClientService apiWorkoutPlannerClientService, WorkoutMapper workoutMapper, WorkoutPlanRepo workoutPlanRepository) {
        this.apiWorkoutPlannerClientService = apiWorkoutPlannerClientService;
        this.workoutMapper = workoutMapper;
        this.workoutPlanRepository = workoutPlanRepository;
    }

    @Transactional
    public WorkoutPlan createAndSavePlan(WorkoutPlanDTO workPlanData, User user) throws Exception {
        // додаю дані з характеристики користувача
        workPlanData.setGoal(user.getUserCharacteristic().getTrainingGoal());
        workPlanData.setFitnessLevel(user.getUserCharacteristic().getFitnessLevel());

        String apiResponse = apiWorkoutPlannerClientService.fetchWorkoutPlan(workPlanData,user.getUserCharacteristic());
        WorkoutPlan workoutPlan = workoutMapper.mapJsonToWorkoutPlan(apiResponse,workPlanData.getTrainingTypes());

        WorkoutPlan.WorkoutPlanPK workoutPlanPK = new WorkoutPlan.WorkoutPlanPK();
        workoutPlanPK.setPlanName(workPlanData.getPlanName());
        workoutPlanPK.setUserUsername(user.getUsername());
        workoutPlan.setWorkoutPlanPK(workoutPlanPK);
        workoutPlan.setUser(user);
        user.getWorkoutPlans().add(workoutPlan);
        return workoutPlanRepository.save(workoutPlan);
    }

    public static List<WorkoutPlanDTO> getWorkoutPlanDTOs(List<WorkoutPlan> workoutPlans) {
        List<WorkoutPlanDTO> workoutPlanDTOs = new ArrayList<>();
        for (WorkoutPlan workoutPlan : workoutPlans) {
            workoutPlanDTOs.add(new WorkoutPlanDTO(workoutPlan));
        }
        return workoutPlanDTOs;
    }
}
