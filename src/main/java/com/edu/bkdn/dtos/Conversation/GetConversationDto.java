package com.edu.bkdn.dtos.Conversation;

import com.edu.bkdn.dtos.Message.GetLastMessageDto;
import com.edu.bkdn.dtos.Participant.GetParticipantDto;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class GetConversationDto {

    @NotEmpty
    private Long id;

    @NotNull
    private String title;

    @NotEmpty
    private Long creatorId;

    @NotEmpty
    private String channelId;

    private String urlAvatar;

    private GetLastMessageDto lastMessage;

    private List<GetParticipantDto> participants;

    private String updatedAt;
}
