package com.edu.bkdn.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "conversation")
public class Conversation extends BaseEntity implements Serializable {

    @Column(columnDefinition = "nvarchar(100) not null")
    private String title;

    @Column(nullable = false)
    private int creatorId;

    @Column(nullable = false)
    private int channelId;

    @OneToMany(mappedBy = "conversation", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Message> messages;
    @OneToMany(mappedBy = "conversation", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Participant> participants;
}
