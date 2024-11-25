package com.example.trainingsite.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Objects;

@Entity
@Data
@Validated
@NoArgsConstructor
public class UserCharacteristic {

    @Id
    private String username;

    @OneToOne(optional = false)
    @PrimaryKeyJoinColumn
    private User user;

    @NotNull(message = "Weight cannot be null")
    @Positive(message = "Weight must be a positive number")
    private Double weight; // вага (кг)

    @NotNull(message = "Height cannot be null")
    @Positive(message = "Height must be a positive number")
    private Double height; // ріст (см)


    @NotNull(message = "Gender cannot be null")
    @Size(min = 1, message = "Gender must not be empty")
    private String gender; // стать

    @NotNull(message = "Training goal cannot be null")
    @Size(min = 1, message = "Training goal must not be empty")
    private String trainingGoal; // мета тренування

    @NotNull(message = "Fitness level cannot be null")
    @Size(min = 1, message = "Fitness level must not be empty")
    private String fitnessLevel; // рівень фізичної підготовки

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "characteristic_user_healconditions", joinColumns = @JoinColumn(name = "user_username"))
    @Column(name = "health_conditions")
    private List<String> healthConditions;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserCharacteristic that = (UserCharacteristic) o;
        return Objects.equals(username, that.username) && Objects.equals(weight, that.weight) && Objects.equals(height, that.height) && Objects.equals(gender, that.gender) && Objects.equals(trainingGoal, that.trainingGoal) && Objects.equals(fitnessLevel, that.fitnessLevel) && Objects.equals(healthConditions, that.healthConditions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, weight, height, gender, trainingGoal, fitnessLevel, healthConditions);
    }
}
