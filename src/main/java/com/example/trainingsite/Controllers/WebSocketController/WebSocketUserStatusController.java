package com.example.trainingsite.Controllers.WebSocketController;


import com.example.trainingsite.entity.User;
import com.example.trainingsite.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.util.HashMap;

@Controller
@RequiredArgsConstructor
public class WebSocketUserStatusController {

    @Autowired
    private UserRepo userRepo;

    private HashMap<String, Integer> countUserAuthentication = new HashMap<>();



    @MessageMapping("/change.user.status")
    public void changeUserStatus(@Payload String status,Authentication authentication){
        if(status.equals("ONLINE")){
            countUserAuthentication.put(authentication.getName(), countUserAuthentication.getOrDefault(authentication.getName(),0) + 1);
            userRepo.updateStatusByLogin(((User) authentication.getPrincipal()).getUsername(), User.Status.valueOf(status));
        } else if(status.equals("OFFLINE")){
            int countAuthentication = countUserAuthentication.getOrDefault(authentication.getName(),0);
            if(countAuthentication > 1){
               countUserAuthentication.put(authentication.getName(), countUserAuthentication.get(authentication.getName()) - 1);
            } else {
                countUserAuthentication.remove(authentication.getName());
                userRepo.updateStatusByLogin(((User) authentication.getPrincipal()).getUsername(), User.Status.valueOf(status));
            }
        }
    }

}
