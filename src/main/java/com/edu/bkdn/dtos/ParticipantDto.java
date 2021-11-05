package com.edu.bkdn.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantDto {
    @NotEmpty
    private long participantId;
    @NotEmpty
    private Date createAt;
    @NotEmpty
    private Date updateAt;
    @NotEmpty
    private long userId;
    @NotEmpty
    private long conversationId;
}
