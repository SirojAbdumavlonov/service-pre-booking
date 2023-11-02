package com.example.preordering.controller;

import com.example.preordering.entity.ChatMessage;
import com.example.preordering.entity.Conversations;
import com.example.preordering.model.ConversationsView;
import com.example.preordering.model.MessageRequest;
import com.example.preordering.model.MessageResponse;
import com.example.preordering.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;
    @MessageMapping("/chat")
    public void sendMessage(@Payload MessageRequest messageRequest){
        String recipient = "siroj_employer";
        ChatMessage chatMessage =
                chatService.sendAndSaveTheMessage(recipient, messageRequest);

        messagingTemplate.convertAndSend("/topic", chatMessage);
    }
    @GetMapping("/{username}/chats")
    public ResponseEntity<?> getContactsList(@PathVariable String username,
                                             @RequestParam(name = "conid", required = false) Long conversationId){

        if(conversationId == null)  {
            List<ConversationsView> getContacts =
                    chatService.getContacts(username);

            return ResponseEntity.ok(getContacts);
        }
        else {
            List<MessageResponse> getMessages =
                    chatService.getMessagesOfContact(conversationId);

            return ResponseEntity.ok(getMessages);
        }
    }


}
