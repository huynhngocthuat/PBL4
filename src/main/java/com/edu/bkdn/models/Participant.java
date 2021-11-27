package com.edu.bkdn.models;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "participant")
public class Participant extends BaseEntity implements Serializable {

    @ManyToOne
    @JoinColumn(name = "id_user")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;
    @ManyToOne
    @JoinColumn(name = "id_conversation")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Conversation conversation;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ParticipantType participantType;
}
