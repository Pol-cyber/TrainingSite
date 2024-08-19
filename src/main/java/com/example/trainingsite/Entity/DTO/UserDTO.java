package com.example.trainingsite.Entity.DTO;

import com.example.trainingsite.Entity.MessageUserToUser;
import com.example.trainingsite.Entity.User;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class UserDTO {

    private String username;
    private String email;
    private byte[] image;
    private String role;
    private User.Status status;
    private Map<String,Integer> unreadMessage;


    public UserDTO(String username, String email, byte[] image, String role, List<MessageUserToUser> unreadMessage, User.Status status) {
        this.username = username;
        this.email = email;
        this.image = image;
        this.role = role;
        if(unreadMessage == null){
            this.unreadMessage = null;
        } else {
            this.unreadMessage = new HashMap<>();
            for (MessageUserToUser messageUserToUser : unreadMessage) {
                this.unreadMessage.put(messageUserToUser.getSenderId(), this.unreadMessage.get(messageUserToUser.getSenderId()) == null ? 1 : this.unreadMessage.get(messageUserToUser.getSenderId()) + 1);
            }
        }
        this.status = status;
    }
}
