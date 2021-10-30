package com.edu.bkdn.repository;

import com.edu.bkdn.domain.participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface participantRepository extends JpaRepository<participant, Long> {
}
