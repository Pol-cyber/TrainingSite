package com.example.trainingsite.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "workout_day_id")
    private WorkoutDay workoutDay;

    @Override
    public String toString() {
        return "Exercise{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", duration='" + duration + '\'' +
                ", repetitions='" + repetitions + '\'' +
                ", sets='" + sets + '\'' +
                ", equipment='" + equipment + '\'' +
                '}';
    }
}
