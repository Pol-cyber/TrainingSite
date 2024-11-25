package com.example.trainingsite.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "workout_day")
public class WorkoutDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String day;

    @OneToMany(mappedBy = "workoutDay", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Exercise> exercises;

    @JsonIgnore
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "workout_plan_name", referencedColumnName = "plan_name"),
            @JoinColumn(name = "workout_plan_username", referencedColumnName = "user_username")
    })
    private WorkoutPlan workoutPlan;

    @Override
    public String toString() {
        return "WorkoutDay{" +
                "id=" + id +
                "workoutPlan=" + workoutPlan.getWorkoutPlanPK().getPlanName() +
                ", day='" + day + '\'' +
                ", exercises=" + exercises +
                '}';
    }
}
