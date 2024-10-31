package com.example.carrivemessage.service.api;

import com.manage.carrive.dto.MessageDto;
import com.manage.carrive.response.MessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface CarriveMessageServiceApi {
    ResponseEntity<MessageResponse> initConversation(String idReceiver);
    ResponseEntity<MessageResponse> listConversationsByUser();
    ResponseEntity<MessageResponse> sendMessage(String idConversation, MessageDto message);
}
