package com.edu.bkdn.dtos.Message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class CreateMessageDto {

    @NotEmpty
    private Long userId;

    @NotEmpty
    private String content;

    @NotEmpty
    private Long conversationId;

    @NotNull
    private Timestamp createdAt;

}
