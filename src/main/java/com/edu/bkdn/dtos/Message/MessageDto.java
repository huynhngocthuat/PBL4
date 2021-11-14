package com.edu.bkdn.dtos.Message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.sql.Timestamp;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class MessageDto {
    @NotEmpty
    private Long messageId;
    @NotEmpty
    private String message;
    @NotEmpty
    private Timestamp createdAt;
    @NotEmpty
    private Timestamp updatedAt;
    @NotEmpty
    private Long senderId;
    @NotEmpty
    private Long conversationId;
}
