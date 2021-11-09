package com.edu.bkdn.dtos.Message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetMessageDto {

    @NotNull
    @NotBlank
    private String message;

    @NotNull
    private Long senderId;

    @NotNull
    private Long conversationId;

    @NotNull
    private Timestamp createAt;

    @NotNull
    private Timestamp updateAt;

    @NotNull
    private Date deletedAt;
}
