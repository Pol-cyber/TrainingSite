package com.example.trainingsite.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "workout_plan")
public class WorkoutPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_username")
    private User user;

    private String goal;
    private String fitnessLevel;
    private Integer totalWeeks;
    private Integer daysPerWeek;
    private Integer sessionDuration;
    private String seo_title;
    private String seo_content;
    @Temporal(TemporalType.DATE)
    @Column(updatable = false)
    private Date date = new Date();


    @ElementCollection
    @CollectionTable(name = "training_types", joinColumns = @JoinColumn(name = "workout_plan_id"))
    @Column(name = "type")
    private List<String> trainingTypes;

    @OneToMany(mappedBy = "workoutPlan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WorkoutDay> workoutDays;

}

