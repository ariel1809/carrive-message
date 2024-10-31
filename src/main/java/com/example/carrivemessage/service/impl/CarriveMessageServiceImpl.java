package com.example.carrivemessage.service.impl;

import com.example.carrivemessage.security.JwtRequestFilter;
import com.example.carrivemessage.service.api.CarriveMessageServiceApi;
import com.manage.carrive.dto.MessageDto;
import com.manage.carrive.entity.Conversation;
import com.manage.carrive.entity.Driver;
import com.manage.carrive.entity.Message;
import com.manage.carrive.entity.Passenger;
import com.manage.carrive.response.MessageResponse;
import com.manage.carriveutility.repository.ConversationRepository;
import com.manage.carriveutility.repository.DriverRepository;
import com.manage.carriveutility.repository.MessageRepository;
import com.manage.carriveutility.repository.PassengerRepository;
import com.manage.carrive.enumeration.CodeResponseEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class CarriveMessageServiceImpl implements CarriveMessageServiceApi {

    private final Logger logger = LoggerFactory.getLogger(CarriveMessageServiceImpl.class);
    private static final ZoneId zoneId = ZoneId.systemDefault();

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public ResponseEntity<MessageResponse> initConversation(String idReceiver) {
        MessageResponse messageResponse = new MessageResponse();
        try {

            Driver driver = JwtRequestFilter.driver;
            Passenger passenger = JwtRequestFilter.passenger;
            Conversation conversation;
            if (idReceiver == null) {
                messageResponse.setData(null);
                messageResponse.setCode(CodeResponseEnum.CODE_NULL.getCode());
                messageResponse.setMessage("id receiver is null");
                return new ResponseEntity<>(messageResponse, HttpStatus.OK);
            }
            if (passenger != null) {
                Driver driver1 = driverRepository.findById(idReceiver).orElse(null);
                if (driver1 == null) {
                    messageResponse.setData(null);
                    messageResponse.setCode(CodeResponseEnum.CODE_NULL.getCode());
                    messageResponse.setMessage("driver is null");
                    return new ResponseEntity<>(messageResponse, HttpStatus.OK);
                }
                conversation = conversationRepository.findByUser1AndUser2(driver1, passenger).orElse(null);
                if (conversation == null) {
                    Conversation conversation1 = new Conversation();
                    conversation1.setUser1(driver1);
                    conversation1.setUser2(passenger);
                    conversation1.setCreatedAt(LocalDateTime.now(zoneId));
                    conversation = conversationRepository.save(conversation1);
                }
            } else if (driver != null) {
                Passenger passenger1 = passengerRepository.findById(idReceiver).orElse(null);
                if (passenger1 == null) {
                    messageResponse.setData(null);
                    messageResponse.setCode(CodeResponseEnum.CODE_NULL.getCode());
                    messageResponse.setMessage("passenger is null");
                    return new ResponseEntity<>(messageResponse, HttpStatus.OK);
                }
                conversation = conversationRepository.findByUser1AndUser2(driver, passenger1).orElse(null);
                if (conversation == null) {
                    Conversation conversation1 = new Conversation();
                    conversation1.setUser1(driver);
                    conversation1.setUser2(passenger1);
                    conversation1.setCreatedAt(LocalDateTime.now(zoneId));
                    conversation = conversationRepository.save(conversation1);
                }
            }else {
                messageResponse.setData(null);
                messageResponse.setCode(CodeResponseEnum.CODE_NULL.getCode());
                messageResponse.setMessage("user not found");
                return new ResponseEntity<>(messageResponse, HttpStatus.OK);
            }
            messageResponse.setCode(CodeResponseEnum.CODE_SUCCESS.getCode());
            messageResponse.setMessage("success");
            messageResponse.setData(conversation);
            return new ResponseEntity<>(messageResponse, HttpStatus.OK);

        }catch (Exception e) {
            logger.error(e.getMessage());
            messageResponse.setMessage(e.getMessage());
            messageResponse.setCode(CodeResponseEnum.CODE_ERROR.getCode());
            messageResponse.setData(null);
            return new ResponseEntity<>(messageResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<MessageResponse> listConversationsByUser() {
        MessageResponse messageResponse = new MessageResponse();
        try {

            Driver driver = JwtRequestFilter.driver;
            Passenger passenger = JwtRequestFilter.passenger;
            List<Conversation> conversations;
            if (driver != null) {
                conversations = conversationRepository.findByUser1(driver);
                if (conversations.isEmpty()){
                    messageResponse.setData(null);
                    messageResponse.setCode(CodeResponseEnum.CODE_NULL.getCode());
                    messageResponse.setMessage("list conversations is empty");
                    return new ResponseEntity<>(messageResponse, HttpStatus.OK);
                }
            } else if (passenger != null) {
                conversations = conversationRepository.findByUser2(passenger);
                if (conversations.isEmpty()){
                    messageResponse.setData(null);
                    messageResponse.setCode(CodeResponseEnum.CODE_NULL.getCode());
                    messageResponse.setMessage("list conversations is empty");
                    return new ResponseEntity<>(messageResponse, HttpStatus.OK);
                }
            }else {
                messageResponse.setData(null);
                messageResponse.setCode(CodeResponseEnum.CODE_NULL.getCode());
                messageResponse.setMessage("user not found");
                return new ResponseEntity<>(messageResponse, HttpStatus.OK);
            }
            messageResponse.setCode(CodeResponseEnum.CODE_SUCCESS.getCode());
            messageResponse.setMessage("success");
            messageResponse.setData(conversations);
            return new ResponseEntity<>(messageResponse, HttpStatus.OK);

        }catch (Exception e) {
            logger.error(e.getMessage());
            messageResponse.setMessage(e.getMessage());
            messageResponse.setCode(CodeResponseEnum.CODE_ERROR.getCode());
            messageResponse.setData(null);
            return new ResponseEntity<>(messageResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<MessageResponse> sendMessage(String idConversation, MessageDto message) {
        MessageResponse messageResponse = new MessageResponse();
        try {

            Driver driver = JwtRequestFilter.driver;
            Passenger passenger = JwtRequestFilter.passenger;
            if (message == null){
                messageResponse.setData(null);
                messageResponse.setCode(CodeResponseEnum.CODE_NULL.getCode());
                messageResponse.setMessage("message is null");
                return new ResponseEntity<>(messageResponse, HttpStatus.OK);
            }
            if (message.getContent() == null){
                messageResponse.setData(null);
                messageResponse.setCode(CodeResponseEnum.CODE_NULL.getCode());
                messageResponse.setMessage("message not found");
                return new ResponseEntity<>(messageResponse, HttpStatus.OK);
            }
            if (idConversation == null) {
                messageResponse.setData(null);
                messageResponse.setCode(CodeResponseEnum.CODE_NULL.getCode());
                messageResponse.setMessage("id conversation is null");
                return new ResponseEntity<>(messageResponse, HttpStatus.OK);
            }
            Conversation conversation = conversationRepository.findById(idConversation).orElse(null);
            if (conversation == null) {
                messageResponse.setData(null);
                messageResponse.setCode(CodeResponseEnum.CODE_NULL.getCode());
                messageResponse.setMessage("conversation not found");
                return new ResponseEntity<>(messageResponse, HttpStatus.OK);
            }
            Message message1 = new Message();
            message1.setContent(message.getContent());
            message1.setSendDate(LocalDate.now());
            message1.setSendTime(LocalTime.now(zoneId));
            if (driver != null) {
                message1.setSender(driver);
                Passenger passenger1 = conversation.getUser2();
                message1.setReceiver(passenger1);
                message1 = messageRepository.save(message1);
                conversation.getMessages().add(message1);
                conversation = conversationRepository.save(conversation);
            } else if (passenger != null) {
                message1.setSender(passenger);
                Driver driver1 = conversation.getUser1();
                message1.setReceiver(driver1);
                message1 = messageRepository.save(message1);
                conversation.getMessages().add(message1);
                conversation = conversationRepository.save(conversation);
            }else {
                messageResponse.setData(null);
                messageResponse.setCode(CodeResponseEnum.CODE_NULL.getCode());
                messageResponse.setMessage("user not found");
                return new ResponseEntity<>(messageResponse, HttpStatus.OK);
            }
            messageResponse.setCode(CodeResponseEnum.CODE_SUCCESS.getCode());
            messageResponse.setMessage("success");
            messageResponse.setData(conversation);
            return new ResponseEntity<>(messageResponse, HttpStatus.OK);

        }catch (Exception e) {
            logger.error(e.getMessage());
            messageResponse.setMessage(e.getMessage());
            messageResponse.setCode(CodeResponseEnum.CODE_ERROR.getCode());
            messageResponse.setData(null);
            return new ResponseEntity<>(messageResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
