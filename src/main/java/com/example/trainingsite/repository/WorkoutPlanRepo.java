package com.example.trainingsite.repository;

import com.example.trainingsite.entity.WorkoutPlan;
import org.springframework.data.repository.CrudRepository;

public interface WorkoutPlanRepo extends CrudRepository<WorkoutPlan, WorkoutPlan.WorkoutPlanPK> {


}
