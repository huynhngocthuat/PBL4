package com.edu.bkdn.dtos.Conversation;

import com.edu.bkdn.dtos.Message.GetMessageDto;
import com.edu.bkdn.dtos.Participant.GetParticipantDto;
import com.edu.bkdn.models.Message;
import com.edu.bkdn.models.Participant;
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
    private Long channelId;

    private List<GetMessageDto> messages;

    private List<GetParticipantDto> participants;
}
