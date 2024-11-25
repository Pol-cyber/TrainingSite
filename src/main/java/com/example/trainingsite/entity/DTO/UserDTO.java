package com.example.trainingsite.entity.DTO;

import com.example.trainingsite.entity.MessageUserToUser;
import com.example.trainingsite.entity.User;
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

    private UserDTO(Builder builder) {
        this.username = builder.username;
        this.email = builder.email;
        this.image = builder.image;
        this.role = builder.role;
        this.unreadMessage = builder.unreadMessage;
        this.status = builder.status;
    }

    public static class Builder {
        private String username;
        private String email;
        private byte[] image;
        private String role;
        private User.Status status;
        private Map<String, Integer> unreadMessage = new HashMap<>();

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setImage(byte[] image) {
            this.image = image;
            return this;
        }

        public Builder setRole(String role) {
            this.role = role;
            return this;
        }

        public Builder setStatus(User.Status status) {
            this.status = status;
            return this;
        }

        public Builder setUnreadMessage(List<MessageUserToUser> unreadMessageList) {
            if (unreadMessageList != null) {
                for (MessageUserToUser messageUserToUser : unreadMessageList) {
                    this.unreadMessage.put(
                            messageUserToUser.getSenderId(),
                            this.unreadMessage.getOrDefault(messageUserToUser.getSenderId(), 0) + 1
                    );
                }
            }
            return this;
        }

        public UserDTO build() {
            return new UserDTO(this);
        }
    }
}
