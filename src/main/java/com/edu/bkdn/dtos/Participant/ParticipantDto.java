package com.edu.bkdn.dtos.Participant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class ParticipantDto {
    @NotEmpty
    private Long participantId;
    @NotEmpty
    private Timestamp createAt;
    @NotEmpty
    private Timestamp updateAt;
    @NotEmpty
    private Long userId;
    @NotEmpty
    private Long conversationId;
}
