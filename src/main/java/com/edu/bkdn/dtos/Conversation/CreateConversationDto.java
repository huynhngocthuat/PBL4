package com.edu.bkdn.dtos.Conversation;

import com.edu.bkdn.dtos.Participant.CreateParticipantDto;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class CreateConversationDto {
    @NotEmpty
    private Long creatorId;
    @NotEmpty
    private String channelId;

    private String urlAvatar;

    private List<CreateParticipantDto> participants;
}
