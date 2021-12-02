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
    private String userPhone;

    @NotNull
    private String contactPhone;

    @NotNull
    private boolean isAccepted;
}
