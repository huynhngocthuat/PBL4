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
@Table(name = "message")
public class Message extends BaseEntity implements Serializable {

    @Column(columnDefinition = "nvarchar(200) not null")
    private String message;


    @ManyToOne
    @JoinColumn(name = "id_sender")
    private User user;
    @ManyToOne
    @JoinColumn(name = "id_conversation")
    private Conversation conversation;

    @OneToMany(mappedBy = "message", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Attachment> attachments;

}
