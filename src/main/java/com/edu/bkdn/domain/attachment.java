package com.edu.bkdn.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "attachment")
public class attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long attachmentId;
    @Column(columnDefinition = "nvarchar(200)")
    private String thumbUrl;
    @Column(columnDefinition = "nvarchar(200)")
    private String fileUrl;
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date createAt;
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date updateAt;

    @ManyToOne
    @JoinColumn(name = "messageId")
    private message message;
}
