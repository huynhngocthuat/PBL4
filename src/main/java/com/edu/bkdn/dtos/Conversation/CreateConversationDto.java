package com.edu.bkdn.dtos.Conversation;

import com.edu.bkdn.dtos.Participant.CreateParticipantDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Validated
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateConversationDto {
    @NotEmpty
    private Long creatorId;
    @NotEmpty
    private String channelId;

    private String urlAvatar;

    private List<CreateParticipantDto> participants;
}
