package com.example.trainingsite.repository;

import com.example.trainingsite.Entity.User;
import com.example.trainingsite.Entity.WorkoutPlan;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface WorkoutPlanRepo extends CrudRepository<WorkoutPlan, WorkoutPlan.WorkoutPlanPK> {


}
