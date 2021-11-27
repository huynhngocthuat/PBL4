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
@Table(name = "message")
public class Message extends BaseEntity implements Serializable {

    @Column(columnDefinition = "nvarchar(200) not null")
    private String content;

    @ManyToOne
    @JoinColumn(name = "id_sender")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_conversation")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Conversation conversation;

    @OneToMany(mappedBy = "message", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Attachment> attachments = new ArrayList<>();

}
