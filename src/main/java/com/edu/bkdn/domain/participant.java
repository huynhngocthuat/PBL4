package com.edu.bkdn.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "participant")
public class participant implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long participantId;
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date createAt;
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date updateAt;

    @ManyToOne
    @JoinColumn(name = "userId")
    private user user;
    @ManyToOne
    @JoinColumn(name = "conversationId")
    private conversation conversation;
}
