package com.edu.bkdn.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class ChatMessage {
    @NotEmpty
    private String sender;
    @NotEmpty
    private String content;
    @NotEmpty
    private String idConversation;
}
