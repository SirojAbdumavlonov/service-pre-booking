package com.example.preordering.repository;

import com.example.preordering.entity.Conversations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface ConversationsRepository extends JpaRepository<Conversations, Long> {

    Conversations findByRecipientUserAdminIdAndSender_UserAdminId(Long recipientId, Long senderId);

    List<Conversations> findBySenderUserAdminId(Long id);
}
