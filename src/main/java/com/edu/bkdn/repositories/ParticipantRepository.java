package com.edu.bkdn.repositories;

import com.edu.bkdn.models.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    @Query(
            value = "SELECT * FROM `participant` WHERE id_conversation = ?1 AND id_user = ?2 AND deleted_at IS NULL;",
            nativeQuery = true
    )
    Optional<Participant> findParticipantByUserIdAndConversationIdAndDeletedAtIsNull(long conversationId, long userId);

    @Query(
            value = "SELECT * FROM `participant` WHERE id_conversation = ?1;",
            nativeQuery = true
    )
    List<Participant> findAllByConversationId(long conversationId);
}
