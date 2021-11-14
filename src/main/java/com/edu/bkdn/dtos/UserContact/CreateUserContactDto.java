package com.edu.bkdn.dtos.UserContact;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateUserContactDto {

    @NotNull
    private Long userId;
    @NotNull
    private Long contactId;
}
