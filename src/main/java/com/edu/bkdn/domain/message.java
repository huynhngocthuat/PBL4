package com.edu.bkdn.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "message")
public class message implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long messageId;
    @Column(columnDefinition = "nvarchar(200) not null")
    private String message;
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date createAt;
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date updateAt;

    @ManyToOne
    @JoinColumn(name = "senderId")
    private user user;
    @ManyToOne
    @JoinColumn(name = "conversationId")
    private conversation conversation;

    @OneToMany(mappedBy = "message", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<attachment> attachments;

}
