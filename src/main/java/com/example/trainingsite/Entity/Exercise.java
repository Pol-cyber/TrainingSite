package com.example.trainingsite.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "exercise")
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String duration;
    private String repetitions;
    private String sets;
    private String equipment;

    @ManyToOne
    @JoinColumn(name = "workout_day_id")
    private WorkoutDay workoutDay;
}
