package com.edu.bkdn.models;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
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
    private long creatorId;

    @Column(nullable = false)
    private long channelId;

    @OneToMany(mappedBy = "conversation", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Message> messages = new ArrayList<>();

    @OneToMany(mappedBy = "conversation", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Participant> participants = new ArrayList<>();
}
