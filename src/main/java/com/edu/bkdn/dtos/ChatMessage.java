package com.edu.bkdn.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    @NotEmpty
    private String sender;
    @NotEmpty
    private String content;
    @NotEmpty
    private String idConversation;
}
