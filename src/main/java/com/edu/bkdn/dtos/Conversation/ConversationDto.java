package com.edu.bkdn.dtos.Conversation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.sql.Timestamp;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class ConversationDto {
    @NotEmpty
    private Long conversationId;
    @NotEmpty
    private String title;
    @NotEmpty
    private Long creatorId;
    @NotEmpty
    private String channelId;
    @NotEmpty
    private Timestamp createAt;
    @NotEmpty
    private Timestamp updateAt;
    @NotEmpty
    private Timestamp deleteAt;
}
