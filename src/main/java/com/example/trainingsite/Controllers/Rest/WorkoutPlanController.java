package com.example.trainingsite.Controllers.Rest;


import com.example.trainingsite.Entity.DTO.WorkoutPlanDTO;

import com.example.trainingsite.Entity.User;
import com.example.trainingsite.Entity.WorkoutPlan;
import com.example.trainingsite.service.WorkoutPlanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/workout")
public class WorkoutPlanController {

    private final WorkoutPlanService workoutPlanService;

    public WorkoutPlanController(WorkoutPlanService workoutPlanService) {
        this.workoutPlanService = workoutPlanService;
    }


    @PostMapping("/create")
    public ResponseEntity<String> createWorkoutPlan(@RequestBody WorkoutPlanDTO workoutPlan,
                                                    Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            // Перевірка кількості планів
            List<WorkoutPlan> userPlans = user.getWorkoutPlan();
            if (userPlans.size() >= 3) {
                return new ResponseEntity<>("К-сть графіків не може бути більше 3.\n Будь ласка видаліть один із минулих та повторіть запит.", HttpStatus.FORBIDDEN);
            }

            // Перевірка інтервалу в 21 день
            // Отримання дати останнього плану
            WorkoutPlan latestPlan = userPlans.stream()
                    .max(Comparator.comparing(WorkoutPlan::getDate)) // передбачається, що getDate повертає дату створення
                    .orElse(null);

            if (latestPlan != null) {
                Date safeDate = new Date(latestPlan.getDate().getTime());
                LocalDate dateOfLastPlan = safeDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                if (ChronoUnit.DAYS.between(dateOfLastPlan, LocalDate.now()) < 21) {
                    return new ResponseEntity<>("З часу створення останнього графіку ще не пройшло 21 день.", HttpStatus.FORBIDDEN);
                }
            }

            // Якщо всі перевірки пройдені, створюємо новий план
            WorkoutPlan newPlan = workoutPlanService.createAndSavePlan(workoutPlan, user);
        } catch (Exception e){
            return new ResponseEntity<>("Помилка під час створення графіку", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Графік успішно створено", HttpStatus.OK);
    }


}
