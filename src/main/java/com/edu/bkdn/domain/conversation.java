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
@Table(name = "conversation")
public class conversation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long conversationId;
    @Column(columnDefinition = "nvarchar(100) not null")
    private String title;
    @Column(nullable = false)
    private int creatorId;
    @Column(nullable = false)
    private int channelId;
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date createAt;
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date updateAt;
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date deleteAt;

    @OneToMany(mappedBy = "conversation", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<message> messages;
    @OneToMany(mappedBy = "conversation", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<participant> participants;
}
