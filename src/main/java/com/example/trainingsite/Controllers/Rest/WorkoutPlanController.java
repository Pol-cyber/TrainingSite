package com.example.trainingsite.Controllers.Rest;


import com.example.trainingsite.entity.DTO.WorkoutPlanDTO;

import com.example.trainingsite.entity.User;
import com.example.trainingsite.entity.WorkoutDay;
import com.example.trainingsite.entity.WorkoutPlan;
import com.example.trainingsite.repository.WorkoutPlanRepo;
import com.example.trainingsite.service.WorkoutPlanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/workout")
public class WorkoutPlanController {

    private final WorkoutPlanService workoutPlanService;
    private final WorkoutPlanRepo workoutPlanRepo;

    public WorkoutPlanController(WorkoutPlanService workoutPlanService, WorkoutPlanRepo workoutPlanRepo) {
        this.workoutPlanService = workoutPlanService;
        this.workoutPlanRepo = workoutPlanRepo;
    }


    @PostMapping("/create")
    public ResponseEntity<String> createWorkoutPlan(@RequestBody WorkoutPlanDTO workoutPlan,
                                                    Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            // Перевірка кількості планів
            List<WorkoutPlan> userPlans = user.getWorkoutPlans();
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


    @DeleteMapping("/delete/{planName}")
    public ResponseEntity<String> deleteWorkoutPlan(@PathVariable String planName,Authentication authentication) {
        try{
            User user = (User) authentication.getPrincipal();
            if(user.getWorkoutPlans().size() == 1){
                return new ResponseEntity<>("Ви не можете видалити єдиний доступний графік", HttpStatus.FORBIDDEN);
            }

            WorkoutPlan planToDelete = user.getWorkoutPlans().stream()
                    .filter(plan -> plan.getWorkoutPlanPK().getPlanName().equals(planName))
                    .findFirst()
                    .orElse(null);

            if (planToDelete == null) {
                return new ResponseEntity<>("План не знайдено", HttpStatus.NOT_FOUND);
            }

            user.getWorkoutPlans().remove(planToDelete);

            // Save changes to the user (cascade will handle deletion)
            workoutPlanRepo.delete(planToDelete);
        } catch (Exception e){
            return new ResponseEntity<>("Помилка під час видалення графіку", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Графік успішно видалено", HttpStatus.OK);
    }

    @GetMapping("/details/{planName}")
    public ResponseEntity<List<WorkoutDay>> getWorkoutPlanDetails(@PathVariable String planName, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        WorkoutPlan.WorkoutPlanPK pk = new WorkoutPlan.WorkoutPlanPK(planName,user.getUsername());
        Optional<WorkoutPlan> workoutPlan = workoutPlanRepo.findById(pk);
        if (workoutPlan.isPresent()) {
            List<WorkoutDay> workoutDays = workoutPlan.get().getWorkoutDays();
            return new ResponseEntity<>(workoutDays, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

}
