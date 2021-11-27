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

    @Column()
    private long creatorId;

    @Column(nullable = false)
    private String channelId;

    @Column(columnDefinition = "nvarchar(255) default 'https://dummyimage.com/600x600/000/fff&text=Conversation'")
    private String urlAvatar = "https://dummyimage.com/600x600/000/fff&text=Conversation";

    @OneToMany(mappedBy = "conversation", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Message> messages = new ArrayList<>();

    @OneToMany(mappedBy = "conversation", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Participant> participants = new ArrayList<>();
}
