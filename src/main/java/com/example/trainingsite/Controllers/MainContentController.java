package com.example.trainingsite.Controllers;


import com.example.trainingsite.Entity.News;
import com.example.trainingsite.Entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

@Controller
@RequestMapping("/content")
public class MainContentController {

    @ModelAttribute
    public void hasTrainingPlan(Model model, Authentication auth) {
        User user = (User) auth.getPrincipal();
        model.addAttribute("hasTrainingPlan", true);
        if(user.getUserCharacteristic() != null) {
            model.addAttribute("haveCharacteristics", true);
        }
    }

    @GetMapping
    public String mainContentGet(){
       return "mainContent";
    }

}

