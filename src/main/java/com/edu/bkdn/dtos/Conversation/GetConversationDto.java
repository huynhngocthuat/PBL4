package com.edu.bkdn.dtos.Conversation;

import com.edu.bkdn.dtos.Message.GetLastMessageDto;
import com.edu.bkdn.dtos.Participant.GetParticipantDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Validated
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetConversationDto {

    @NotNull
    @Positive
    private Long id;

    @NotNull
    private String title;

    @NotNull
    @Positive
    private Long creatorId;

    @NotNull
    @Positive
    private String channelId;

    private String urlAvatar;

    @NotNull
    private GetLastMessageDto lastMessage;

    private List<GetParticipantDto> participants;
}
