package com.edu.bkdn.dtos.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;


@Validated
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CreateUserDto {

    @NotNull
    @NotBlank
    @Email(message = "Email is invalid")
    private String email;

    @NotNull
    @NotBlank
    @Size(min=10, max=11)
    @Pattern(regexp="(^$|[0-9]{10})")
    private String phone;

    @NotNull
    @NotBlank
    @Length(min = 6, max = 30, message = "Password length must be more than 6 digits")
    private String password;

    @NotNull
    @NotBlank
    private String firstName;

    @NotNull
    @NotBlank
    private String surname;

    @NotNull
    private Boolean isActive;
}
