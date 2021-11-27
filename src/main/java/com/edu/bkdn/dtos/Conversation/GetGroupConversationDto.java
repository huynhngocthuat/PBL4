package com.edu.bkdn.dtos.Conversation;

import com.edu.bkdn.dtos.Contact.GetConversationContactDto;
import com.edu.bkdn.dtos.Participant.GetParticipantDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class GetGroupConversationDto {
    @NotEmpty
    private Long id;

    @NotNull
    private String title;

    @NotEmpty
    private Long creatorId;

    @NotEmpty
    private String channelId;

    private String urlAvatar;

    private List<GetConversationContactDto> participants;
}
