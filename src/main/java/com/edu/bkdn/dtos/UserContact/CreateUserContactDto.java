package com.edu.bkdn.dtos.UserContact;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class CreateUserContactDto {

    @NotNull
    private Long userId;
    @NotNull
    private Long contactId;
}
