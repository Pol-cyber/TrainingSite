package com.example.trainingsite.Controllers.Rest;

import com.example.trainingsite.Entity.DTO.UserDTO;
import com.example.trainingsite.Entity.MessageGeneralChat;
import com.example.trainingsite.Entity.MessageUserToUser;
import com.example.trainingsite.repository.GeneralChatRepo;
import com.example.trainingsite.repository.MUserToUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ChatController {

    @Autowired
    private GeneralChatRepo generalChatRepo;

    @Autowired
    private MUserToUserRepo mUserToUserRepo;

    @GetMapping("/api/chat/general/last10")
    public List<MessageGeneralChat> getLast10GeneralMessages() {
        Pageable pageable = PageRequest.of(0,10);
        return generalChatRepo.findLast10Messages(pageable);
    }

    @GetMapping("/api/chat/toUser/last10")
    public List<MessageUserToUser> getLast10UserToUserMessage(@RequestParam(name = "senderId") String senderId,@RequestParam(name = "receiverId") String receiverId){
        Pageable pageable = PageRequest.of(0,10);
        return mUserToUserRepo.findLast10MessagesBetweenUsers(senderId,receiverId,pageable);
    }

}

