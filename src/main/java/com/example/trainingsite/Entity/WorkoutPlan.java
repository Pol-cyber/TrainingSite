package com.example.trainingsite.Entity;

import com.example.trainingsite.Entity.DTO.WorkoutPlanDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Data
@Table(name = "workout_plan")
public class WorkoutPlan {

    @Data
    @NoArgsConstructor
    @Embeddable
    public static class WorkoutPlanPK implements Serializable {
        @Column(name = "plan_name")
        private String planName;
        @Column(name = "user_username")
        private String userUsername;

        public WorkoutPlanPK(String planName, String userUsername) {
            this.planName = planName;
            this.userUsername = userUsername;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            WorkoutPlanPK that = (WorkoutPlanPK) o;
            return Objects.equals(planName, that.planName) && Objects.equals(userUsername, that.userUsername);
        }

        @Override
        public int hashCode() {
            return Objects.hash(planName, userUsername);
        }
    }

    @EmbeddedId
    private WorkoutPlanPK workoutPlanPK;

    @ManyToOne
    @JoinColumn(name = "user_username",insertable = false, updatable = false)
    private User user;

    private String goal;
    private String fitnessLevel;
    private Integer totalWeeks;
    private Integer daysPerWeek;
    private Integer sessionDuration;
    private String seo_title;
    @Column(length = 755)
    private String seo_content;
    @Temporal(TemporalType.DATE)
    @Column(updatable = false)
    private Date date = new Date();


    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "training_types", joinColumns = {
            @JoinColumn(name = "workout_plan_name", referencedColumnName = "plan_name"),
            @JoinColumn(name = "workout_user_username", referencedColumnName = "user_username")
    })
    @Column(name = "type")
    private List<String> trainingTypes;

    @OneToMany(mappedBy = "workoutPlan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WorkoutDay> workoutDays;

}

