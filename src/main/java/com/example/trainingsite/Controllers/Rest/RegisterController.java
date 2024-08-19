package com.example.trainingsite.Controllers.Rest;


import com.example.trainingsite.Entity.User;
import com.example.trainingsite.repository.UserRepo;
import lombok.Data;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/register")
public class RegisterController {


    private UserRepo userRepo;
    private PasswordEncoder passwordEncoder;

    public RegisterController(PasswordEncoder passwordEncoder,UserRepo userRepo){
        this.passwordEncoder = passwordEncoder;
        this.userRepo = userRepo;
    }


    @PostMapping(consumes=MediaType.MULTIPART_FORM_DATA_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public RegistrationResponse uploadFile(@RequestParam(required=true, value="image-register",name = "image-register") MultipartFile file,
                                           @RequestParam(name = "email") String email,
                                           @RequestParam(name = "username") String username,
                                           @RequestParam(name = "password") String password,
                                           @RequestParam(name = "confirmPassword") String confirmPassword){
        try {
            byte[] bytes = null;
            if(file.getBytes().length > 0){
                bytes = file.getBytes();
            }
            if (!Pattern.matches("^[\\w\\-\\.]+@[\\w]{2,4}$",email)){
                return new RegistrationResponse(false,"Ваша пошта має невірний формат.");
            }
            if(password.length() < 8 || password.length() > 20){
                return new RegistrationResponse(false,"Пароль повинен бути більше 8 символів та менше 20.");
            }
            if(!password.equals(confirmPassword)){
                return new RegistrationResponse(false,"Пароль не збігається.");
            }
            if(userRepo.findByUsername(username) != null){
                return new RegistrationResponse(false,"Користувач з таким логіном вже зареєстрований.");
            }
            User user = new User(username,passwordEncoder.encode(password),
                email,bytes,"ROLE_USER");
            userRepo.save(user);
        } catch (Exception e){
            return new RegistrationResponse(false,"Сталась помилка під час збереження.");
        }
        return new RegistrationResponse(true, null);
    }

    @Data
    public class RegistrationResponse {
        private boolean success;
        private String message;

        public RegistrationResponse(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

    }

}