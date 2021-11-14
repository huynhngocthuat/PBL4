package com.edu.bkdn.dtos.Message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Validated
@Getter
@Setter
public class GetMessageDto {

    @NotNull
    @NotBlank
    private String message;

    @NotNull
    private Long userId;

    @NotNull
    private Long conversationId;

    @NotNull
    private String createdAt;

    @NotNull
    private String updatedAt;

    @NotNull
    private String deletedAt;
}
