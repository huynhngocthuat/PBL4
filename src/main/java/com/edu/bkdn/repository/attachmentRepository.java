package com.edu.bkdn.repository;

import com.edu.bkdn.domain.attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface attachmentRepository extends JpaRepository<attachment, Long> {
}
