package com.example.preordering.repository;

import com.example.preordering.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query("SELECT C FROM ChatMessage C WHERE C.sender.userAdminId = ?1 AND C.recipient.userAdminId = ?2"
    )
    List<ChatMessage> findBySenderUserAdminIdAndRecipientUserAdminId(Long senderId, Long recipientId);

    @Query(
            "SELECT c FROM ChatMessage c WHERE c.conversations.conversationId = ?1"
    )
    List<ChatMessage> findByConversationsConversationId(Long conversationId);
//    @Query(
//            "SELECT c FROM ChatMessage c"
//    )
//    List<ChatMessage> findAll();
    @Query(
            "SELECT c FROM ChatMessage c WHERE c.conversations.conversationId = ?1 " +
                    "ORDER BY c.timestamp DESC LIMIT 1"
    )
    ChatMessage getLastMessage(Long conversationId);
}
