package com.init.chatroom;

import com.init.chatroom.model.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@CrossOrigin
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage chatMessage) {
        simpMessagingTemplate.convertAndSend("/topic/"+chatMessage.getRoomName()+"/"
                +chatMessage.getRoomCode(),chatMessage);
    }

    @MessageMapping("/chat.addUser")
    public void addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {

        chatMessage.setType(ChatMessage.MessageType.JOIN);
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        headerAccessor.getSessionAttributes().put("roomName", chatMessage.getRoomName());
        headerAccessor.getSessionAttributes().put("roomCode", chatMessage.getRoomCode());

        simpMessagingTemplate.convertAndSend("/topic/"+chatMessage.getRoomName()+"/"
                +chatMessage.getRoomCode(),chatMessage);
    }

}
