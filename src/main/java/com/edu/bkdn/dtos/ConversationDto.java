package com.edu.bkdn.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConversationDto {
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
