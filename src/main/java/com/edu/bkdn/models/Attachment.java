package com.edu.bkdn.models;

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
public class Attachment extends BaseEntity {

    @Column(columnDefinition = "nvarchar(200)")
    private String thumbUrl;

    @Column(columnDefinition = "nvarchar(200)")
    private String fileUrl;

    @ManyToOne
    @JoinColumn(name = "id_message")
    private Message message;
}
