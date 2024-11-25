package com.example.trainingsite.Controllers;


import com.example.trainingsite.entity.News;
import com.example.trainingsite.repository.NewsRep;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping
public class WelcomeController {
    private NewsRep newsRep;

    public WelcomeController(NewsRep newsRep){
        this.newsRep = newsRep;
    }

    @GetMapping("/")
    public String homePage(@RequestParam(name = "error", required = false) String error, Model model){
        if (error != null) {
            if(error.equals("access-denied")){
                model.addAttribute("errorMessage", "Для переходу на " +
                        "/content необхідно авторизуватися.");
            } else {
                model.addAttribute("errorMessage", "Введені дані невірні.");
            }
        }
        return "home";
    }

    @ModelAttribute
    public void getDataTaco(Model model){
        List<News> news = newsRep.findActualNews();
        model.addAttribute("newsList", news);
    }
}

