package com.edu.bkdn.repositories;

import com.edu.bkdn.models.Contact;
import com.edu.bkdn.models.User;
import com.edu.bkdn.models.UserContact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserContactRepository extends JpaRepository<UserContact, Long> {

    Optional<UserContact> findUserContactByUserEqualsAndContactEquals(User user, Contact contact);

    List<UserContact> findAllByUserEquals(User user);
}
