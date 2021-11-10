package com.edu.bkdn.dtos.Conversation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.sql.Timestamp;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConversationDto {
    @NotEmpty
    private Long conversationId;
    @NotEmpty
    private String title;
    @NotEmpty
    private Long creatorId;
    @NotEmpty
    private Long channelId;
    @NotEmpty
    private Timestamp createAt;
    @NotEmpty
    private Timestamp updateAt;
    @NotEmpty
    private Timestamp deleteAt;
}