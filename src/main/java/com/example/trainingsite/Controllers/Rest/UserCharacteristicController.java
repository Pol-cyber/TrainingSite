package com.example.trainingsite.Controllers.Rest;

import com.example.trainingsite.entity.User;
import com.example.trainingsite.entity.UserCharacteristic;
import com.example.trainingsite.repository.CharacteristicRepo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/characteristic")
public class UserCharacteristicController {

    @Autowired
    private CharacteristicRepo characteristicRepo;

    @PostMapping
    public ResponseEntity<String> saveUserCharacteristic(@Valid @RequestBody UserCharacteristic userCharacteristic, Errors errors,
                                                         Authentication authentication){
        if(errors.hasErrors()){
            return new ResponseEntity<>(errors.getAllErrors().toString(), HttpStatus.BAD_REQUEST);
        }
        userCharacteristic.setUsername(((User) authentication.getPrincipal()).getUsername());
        if(userCharacteristic.equals(((User)authentication.getPrincipal()).getUserCharacteristic())){
            return new ResponseEntity<>("Дана характеристика вже збережена", HttpStatus.CONFLICT);
        }
        try {
            characteristicRepo.save(userCharacteristic);
        } catch (Exception e){
            return new ResponseEntity<>("Помилка", HttpStatus.NOT_FOUND);
        }
        ((User) authentication.getPrincipal()).setUserCharacteristic(userCharacteristic);
        return new ResponseEntity<>("Успішно створено", HttpStatus.OK);
    }

}
