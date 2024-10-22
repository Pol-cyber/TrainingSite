package com.example.trainingsite.Entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

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

    @NotNull(message = "Age cannot be null")
    @Positive(message = "Age must be a positive number")
    private Integer age; // вік

    @NotNull(message = "Weight cannot be null")
    @Positive(message = "Weight must be a positive number")
    private Double weight; // вага (кг)

    @NotNull(message = "Height cannot be null")
    @Positive(message = "Height must be a positive number")
    private Double height; // ріст (см)

    private Boolean heartProblems = false; // проблеми із серцем

    @NotNull(message = "Gender cannot be null")
    @Size(min = 1, message = "Gender must not be empty")
    private String gender; // стать

    @NotNull(message = "Training goal cannot be null")
    @Size(min = 1, message = "Training goal must not be empty")
    private String trainingGoal; // мета тренування

    @NotNull(message = "Fitness level cannot be null")
    @Size(min = 1, message = "Fitness level must not be empty")
    private String fitnessLevel; // рівень фізичної підготовки

    private Boolean obesity = false; // присутнє ожиріння
}
