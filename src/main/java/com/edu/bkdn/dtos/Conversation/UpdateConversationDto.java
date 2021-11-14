package com.edu.bkdn.dtos.Conversation;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class UpdateConversationDto {

    @NotNull
    private String title;

    @NotEmpty
    private Long creatorId;

    @NotEmpty
    private String channelId;

    private String urlAvatar;
}
