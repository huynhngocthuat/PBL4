package com.edu.bkdn.repository;

import com.edu.bkdn.domain.message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface messageRepository extends JpaRepository<message, Long> {
}
