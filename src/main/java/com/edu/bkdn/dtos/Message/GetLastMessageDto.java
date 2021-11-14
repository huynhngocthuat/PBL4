package com.edu.bkdn.dtos.Message;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class GetLastMessageDto {

    private String message;

    @NotEmpty
    private Long userId;

    @NotEmpty
    private Long conversationId;

    @NotNull
    private String createdAt;
}
