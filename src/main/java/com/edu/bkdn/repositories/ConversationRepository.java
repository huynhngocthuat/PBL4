package com.edu.bkdn.repositories;

import com.edu.bkdn.models.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    @Query(
            value = "SELECT c.* from conversation c " +
                    "INNER JOIN participant p on c.id = p.id_conversation " +
                    "WHERE p.id_user = ?1 " +
                    "GROUP BY c.id;",
            nativeQuery = true
    )
    List<Conversation> findAllConversationByUserId(Long userId);
}
