package com.edu.bkdn.dtos.Participant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;

@Validated
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetParticipantDto {

    @NotNull
    private Long id;

    @NotNull
    private Long userId;

    @NotNull
    private Long conversationId;
}
