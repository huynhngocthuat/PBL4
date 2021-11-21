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
                    "WHERE u.id = ?1 AND uc.is_accepted = 1 AND uc.deleted_at IS NULL",
            nativeQuery = true
    )
    List<Contact> findAllByUserIdAndIsAcceptedAndDeletedAtIsNull(long userId);

    @Query(
            value = "SELECT c.* FROM contact c  " +
                    "WHERE c.id NOT IN  " +
                    "(SELECT uc.id_contact FROM user_contact uc  " +
                    " WHERE uc.id_user = ?1 AND uc.is_accepted = 1 AND uc.deleted_at IS NULL);",
            nativeQuery = true
    )
    List<Contact> findAllStrangerByUserIdAndIsNotAccepted(long userId);

    @Query(
            value = "SELECT c.* FROM contact c  " +
                    "WHERE c.id IN  " +
                    "(SELECT uc.id_contact FROM user_contact uc  " +
                    " WHERE uc.id_user = ?1 " +
                    "AND uc.is_accepted = 0 AND uc.id_request_sender != ?1 AND uc.deleted_at IS NULL);",
            nativeQuery = true
    )
    List<Contact> findAllByUserIdAndAcceptedIsFalseAndDeletedAtIsNull(long userId);

    @Query(
            value = "SELECT c.* FROM contact c  " +
                    "WHERE c.id IN  " +
                    "(SELECT uc.id_contact FROM user_contact uc  " +
                    "WHERE uc.id_user = ?1 " +
                    "AND uc.deleted_at IS NULL);",
            nativeQuery = true
    )
    List<Contact> findAllRelativeByUserId(long userId);

    Optional<Contact> findContactByPhone(String phoneNumber);

}
