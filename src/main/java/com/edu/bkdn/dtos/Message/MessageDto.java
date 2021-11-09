package com.edu.bkdn.dtos.Message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.sql.Timestamp;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    @NotEmpty
    private Long messageId;
    @NotEmpty
    private String message;
    @NotEmpty
    private Timestamp createAt;
    @NotEmpty
    private Timestamp updateAt;
    @NotEmpty
    private Long senderId;
    @NotEmpty
    private Long conversationId;
}
