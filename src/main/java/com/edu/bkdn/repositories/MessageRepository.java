package com.edu.bkdn.repositories;

import com.edu.bkdn.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query(
            value = "SELECT * FROM `message` WHERE id_conversation = ?1 AND id_user = ?2 AND deleted_at IS NULL",
            nativeQuery = true
    )
    List<Message> findAllByConversationAndUserAndDeletedAtIsNull(Long conversationId, Long userId);
}
