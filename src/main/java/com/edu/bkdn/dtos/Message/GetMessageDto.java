package com.edu.bkdn.dtos.Message;

import com.edu.bkdn.dtos.User.GetUserDto;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Validated
@Getter
@Setter
public class GetMessageDto {

    @NotNull
    @NotBlank
    private String message;

    @NotNull
    private GetUserDto user;

    @NotNull
    private Long conversationId;

    @NotNull
    private String createdAt;

    @NotNull
    private String updatedAt;

    @NotNull
    private String deletedAt;
}
