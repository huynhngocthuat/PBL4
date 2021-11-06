package com.edu.bkdn.dtos.UserContact;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Validated
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetContactDto {
    @NotNull
    @Positive
    private Long id;

    @NotNull
    private String email;

    @NotNull
    private String firstName;

    @NotNull
    private String surname;
}
