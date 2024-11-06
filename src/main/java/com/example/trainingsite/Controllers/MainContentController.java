package com.example.trainingsite.Controllers;


import com.example.trainingsite.Entity.DTO.CharacteristicDTO;
import com.example.trainingsite.Entity.User;
import com.example.trainingsite.Entity.UserCharacteristic;
import com.example.trainingsite.service.WorkoutPlanService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/content")
public class MainContentController {

    @ModelAttribute
    public void hasTrainingPlan(Model model, Authentication auth) {
        User user = (User) auth.getPrincipal();
        if(!((User) auth.getPrincipal()).getWorkoutPlans().isEmpty()) { // перевіряю чи існує план тренування
            model.addAttribute("trainingPlans", WorkoutPlanService.getWorkoutPlanDTOs(((User) auth.getPrincipal()).getWorkoutPlans()));
        }
        if(user.getUserCharacteristic() != null) { // перевіряю чи існує характеристика
            UserCharacteristic userCharacteristic = user.getUserCharacteristic();

            // Створюємо DTO для передачі в модель
            CharacteristicDTO characteristicDTO = new CharacteristicDTO();
            characteristicDTO.setWeight(userCharacteristic.getWeight());
            characteristicDTO.setHeight(userCharacteristic.getHeight());
            characteristicDTO.setGender(userCharacteristic.getGender());
            characteristicDTO.setTrainingGoal(userCharacteristic.getTrainingGoal());
            characteristicDTO.setFitnessLevel(userCharacteristic.getFitnessLevel());
            characteristicDTO.setHealthConditions(userCharacteristic.getHealthConditions());

            // Передаємо DTO в модель
            model.addAttribute("characteristics", characteristicDTO);
        }
    }

    @GetMapping
    public String mainContentGet(){
       return "mainContent";
    }

}

