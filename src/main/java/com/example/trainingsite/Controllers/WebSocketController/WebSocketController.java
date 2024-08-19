package com.example.trainingsite.Controllers.WebSocketController;


import com.example.trainingsite.Entity.MessageGeneralChat;
import com.example.trainingsite.Entity.MessageUserToUser;
import com.example.trainingsite.repository.GeneralChatRepo;
import com.example.trainingsite.repository.MUserToUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private GeneralChatRepo generalChatRepo;

    @Autowired
    private MUserToUserRepo mUserToUserRepo;

    @MessageMapping("/send.general")
    public void sendMessageToGeneral(MessageGeneralChat s){
        generalChatRepo.save(s);
        simpMessagingTemplate.convertAndSend("/topic/chat.general",s);
    }

    @MessageMapping("/send.to.user")
    public void sendMessageToUser(MessageUserToUser m) {
        mUserToUserRepo.save(m);
        simpMessagingTemplate.convertAndSend("/topic/chat."+m.getReceiverId(),m);
    }


    @MessageMapping("/receive.all")
    public void receiveAll(MessageUserToUser messageUserToUser){
        String senderId = messageUserToUser.getSenderId();
        String receiverId = messageUserToUser.getReceiverId();
        mUserToUserRepo.updateAllMessage(senderId,receiverId);
    }

    @MessageMapping("/receive.specific")
    public void receiveSpecific(MessageUserToUser messageUserToUser){
        mUserToUserRepo.updateSpecificMessage(messageUserToUser.getId());
    }
}
