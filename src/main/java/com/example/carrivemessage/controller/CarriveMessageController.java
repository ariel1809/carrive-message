package com.example.carrivemessage.controller;

import com.example.carrivemessage.service.impl.CarriveMessageServiceImpl;
import com.manage.carrive.dto.MessageDto;
import com.manage.carrive.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("message")
public class CarriveMessageController {

    @Autowired
    private CarriveMessageServiceImpl service;

    @PostMapping("init-conversation")
    public ResponseEntity<MessageResponse> initConversation(@RequestParam("id_receiver") String idReceiver) {
        return service.initConversation(idReceiver);
    }

    @PostMapping("list-conversations")
    public ResponseEntity<MessageResponse> listConversations() {
        return service.listConversationsByUser();
    }

    @PostMapping("send-message")
    public ResponseEntity<MessageResponse> sendMessage(@RequestParam("idConversation") String idConversation, @RequestBody MessageDto message) {
        return service.sendMessage(idConversation, message);
    }
}
