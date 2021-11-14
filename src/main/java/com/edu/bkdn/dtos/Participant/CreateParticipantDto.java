package com.edu.bkdn.dtos.Participant;

import com.edu.bkdn.models.ParticipantType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateParticipantDto {

    @NotNull
    private Long userId;

    @NotNull
    private Long conversationId;

    @NotNull
    private ParticipantType participantType;
}
