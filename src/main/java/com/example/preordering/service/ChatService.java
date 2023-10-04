package com.example.preordering.service;

import com.example.preordering.entity.ChatMessage;
import com.example.preordering.entity.Conversations;
import com.example.preordering.entity.UserAdmin;
import com.example.preordering.model.ConversationsView;
import com.example.preordering.model.MessageRequest;
import com.example.preordering.model.MessageResponse;
import com.example.preordering.repository.ChatMessageRepository;
import com.example.preordering.repository.ConversationsRepository;
import com.example.preordering.repository.UserAdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final UserAdminRepository userAdminRepository;
    private final ConversationsRepository conversationsRepository;
    private final ChatMessageRepository chatMessageRepository;

    public ChatMessage sendAndSaveTheMessage(String recipientid, MessageRequest messageRequest) {
        Long recipientId = Long.getLong(recipientid);
        UserAdmin sender =
                userAdminRepository.findByUsername(messageRequest.getSenderUsername());
        UserAdmin recipient =
                userAdminRepository.getReferenceById(recipientId);

        Conversations conversation = conversationsRepository.
                findByRecipientUserAdminIdAndSender_UserAdminId(recipientId, sender.getUserAdminId());

        if (conversation == null){
            Conversations conversations =
                    Conversations.builder()
                            .recipient(recipient)
                            .sender(sender)
                            .build();
        conversationsRepository.save(conversations);
        }
        ChatMessage chatMessage =
                ChatMessage.builder()
                        .sender(sender)
                        .recipient(recipient)
                        .content(messageRequest.getContent())
                        .timestamp(messageRequest.getTimestamp())
                        .conversations(conversation)
                        .build();

        chatMessageRepository.save(chatMessage);

        return chatMessage;
    }
    public List<ConversationsView> getContacts(String senderUsername){
        UserAdmin sender =
                userAdminRepository.findByUsername(senderUsername);

        List<ConversationsView> conversationsViews = new ArrayList<>();
        List <Conversations> conversations =
                conversationsRepository.findBySenderUserAdminId(sender.getUserAdminId());
        for (Conversations conversation: conversations){
            ChatMessage lastMessage = chatMessageRepository.getLastMessage(conversation.getConversationId());
            conversationsViews.add(
                    ConversationsView.builder()
                            .lastMessage(lastMessage.getContent())
                            .timestamp(lastMessage.getTimestamp())
                            .conversations(conversation)
                            .build()
            );
        }
        return conversationsViews;

    }
    public List<MessageResponse> getMessagesOfContact(Long conversationId){
//
        List<ChatMessage> messages =
                chatMessageRepository.findByConversationsConversationId(conversationId);
        if(messages.isEmpty()){
            return new ArrayList<>();
        }
        List<MessageResponse> responses = new ArrayList<>();
        for(ChatMessage message: messages){
            responses.add(
                    new MessageResponse(message.getContent(), message.getTimestamp(), message.getSender().getUsername())
            );
        }
        return responses;
    }

}
