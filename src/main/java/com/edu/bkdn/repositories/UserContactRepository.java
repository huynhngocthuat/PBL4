package com.edu.bkdn.repositories;

import com.edu.bkdn.models.User;
import com.edu.bkdn.models.UserContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserContactRepository extends JpaRepository<UserContact, Long> {

    @Query(
            value = "SELECT * FROM `user_contact` WHERE id_contact = ?1 AND id_user = ?2",
            nativeQuery = true
    )
    Optional<UserContact> findUserContactByUserEqualsAndContactEquals(Long contactId, Long userId);

    List<UserContact> findAllByUserEquals(User user);
}
