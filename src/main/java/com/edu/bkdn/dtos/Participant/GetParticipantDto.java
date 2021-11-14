package com.edu.bkdn.dtos.Participant;

import com.edu.bkdn.models.ParticipantType;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class GetParticipantDto {

    @NotEmpty
    private Long id;

    @NotEmpty
    private Long userId;

    @NotEmpty
    private Long conversationId;

    @NotNull
    private ParticipantType participantType;
}
