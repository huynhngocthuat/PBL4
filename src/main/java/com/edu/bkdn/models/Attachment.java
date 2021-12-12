package com.edu.bkdn.models;
import lombok.*;
import javax.persistence.*;
import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "attachment")
public class Attachment extends BaseEntity implements Serializable {

    @Lob
    private byte[] data;

    @Column(columnDefinition = "nvarchar(200)")
    private String fileName;

    @Column(columnDefinition = "nvarchar(200)")
    private String fileType;

    @OneToOne
    @JoinColumn(name = "id_message")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Message message;
}