package com.edu.bkdn.repositories;

import com.edu.bkdn.models.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    @Query(
            value = "SELECT c.* FROM contact c " +
                    "INNER JOIN user_contact uc ON c.id =  uc.id_contact " +
                    "INNER JOIN user u ON u.id = uc.id_user " +
                    "WHERE u.id = ?1",
            nativeQuery = true
    )
    List<Contact> getContactsByUserId(Long userId);

    Optional<Contact> findContactByPhone(String phoneNumber);
}
