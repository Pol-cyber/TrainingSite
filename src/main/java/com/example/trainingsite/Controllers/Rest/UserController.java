package com.example.trainingsite.Controllers.Rest;

import com.example.trainingsite.Entity.DTO.UserDTO;
import com.example.trainingsite.Entity.User;
import com.example.trainingsite.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepo userRepository; // Залежність для доступу до репозиторію користувачів

    @PostMapping("/online")
    public void setUserOnline(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            userRepository.updateStatusByLogin(((User)authentication.getPrincipal()).getUsername(),User.Status.ONLINE);
        }
    }

    @PostMapping("/offline")
    public void setUserOffline(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            userRepository.updateStatusByLogin(((User)authentication.getPrincipal()).getUsername(),User.Status.OFFLINE);
        }
    }


    @GetMapping("/allUserWithFirstActive")
    public List<UserDTO> getAllUserWithoutActive(Authentication authentication){
        if (authentication != null && authentication.isAuthenticated()) {
            List<User> list = (List<User>) userRepository.findAll();
            LinkedList<UserDTO> userDTOS = new LinkedList<>();
            for(User user : list){
                String currentUser = ((User) authentication.getPrincipal()).getUsername();
                if(Objects.equals(user.getUsername(), currentUser)){
                    userDTOS.addFirst(new UserDTO(user.getUsername(), user.getEmail(),user.getImage(),user.getRole(),user.getUnreadMessage(),user.getStatus()));
                } else {
                    userDTOS.addLast(new UserDTO(user.getUsername(), user.getEmail(),user.getImage(),user.getRole(),null,user.getStatus()));
                }
            }
            return userDTOS;
        }
        return null;
    }
}
