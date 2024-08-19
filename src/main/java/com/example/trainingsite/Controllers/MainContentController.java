package com.example.trainingsite.Controllers;


import com.example.trainingsite.Entity.News;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

@Controller
@RequestMapping("/content")
public class MainContentController {

    @GetMapping
    public String mainContentGet(){
       return "mainContent";
    }
}

