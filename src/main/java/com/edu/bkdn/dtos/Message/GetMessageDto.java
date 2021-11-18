package com.edu.bkdn.dtos.Message;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class GetMessageDto {

    private String content;

    @NotEmpty
    private Long userId;

    @NotEmpty
    private Long conversationId;

    @NotNull
    private Timestamp createdAt;

    @NotNull
    private String updatedAt;

    private String deletedAt;
}
