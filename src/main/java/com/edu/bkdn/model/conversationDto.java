package com.edu.bkdn.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class conversationDto {
    @NotEmpty
    private long conversationId;
    @NotEmpty
    private String title;
    @NotEmpty
    private int creatorId;
    @NotEmpty
    private int channelId;
    @NotEmpty
    private Date createAt;
    @NotEmpty
    private Date updateAt;
    @NotEmpty
    private Date deleteAt;
}
