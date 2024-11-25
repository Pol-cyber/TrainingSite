package com.example.trainingsite.Controllers.Rest;

import com.example.trainingsite.entity.MessageGeneralChat;
import com.example.trainingsite.entity.MessageUserToUser;
import com.example.trainingsite.repository.GeneralChatRepo;
import com.example.trainingsite.repository.MUserToUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.Date;
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

//    @GetMapping("/api/chat/toUser/last10")
//    public List<MessageUserToUser> getLast10UserToUserMessage(@RequestParam(name = "senderId") String senderId,@RequestParam(name = "receiverId") String receiverId){
//        Pageable pageable = PageRequest.of(0,10);
//        return mUserToUserRepo.findLast10MessagesBetweenUsers(senderId,receiverId,pageable);
//    }

    @Transactional
    @GetMapping("/api/chat/toUser/receiveAllUnreadAndLast10Read")
    public List<MessageUserToUser> receiveUnreadAndGetLast10ReadUserToUserMessage(@RequestParam(name = "senderId") String senderId, @RequestParam(name = "receiverId") String receiverId){
        Integer countNewMessage = mUserToUserRepo.receiveAllUnreadMessage(receiverId,senderId);
        Pageable pageable = PageRequest.of(0,10 + countNewMessage);
        return mUserToUserRepo.findLast10ReceiveMessagesBetweenUsers(senderId,receiverId,pageable);
    }

}

