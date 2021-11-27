package com.edu.bkdn.models;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_contact")
public class UserContact extends BaseEntity implements Serializable {

    @ManyToOne
    @JoinColumn(name = "id_user")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;
    @ManyToOne
    @JoinColumn(name = "id_contact")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Contact contact;

    @Column(nullable = false, name = "id_request_sender")
    private long requestSenderId;

    @Column()
    private Boolean isAccepted;

    @Column()
    private String invitationMessage;
}
