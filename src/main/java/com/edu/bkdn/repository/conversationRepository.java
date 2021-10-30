package com.edu.bkdn.repository;

import com.edu.bkdn.domain.conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface conversationRepository extends JpaRepository<conversation, Long> {
}
