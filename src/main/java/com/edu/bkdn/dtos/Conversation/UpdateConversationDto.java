package com.edu.bkdn.dtos.Conversation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Validated
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateConversationDto {

    @NotNull
    private String title;

    @NotNull
    @Positive
    private Long creatorId;

    @NotNull
    @Positive
    private String channelId;

    private String urlAvatar;
}
