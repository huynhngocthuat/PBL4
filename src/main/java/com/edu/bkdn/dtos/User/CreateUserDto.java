package com.edu.bkdn.dtos.User;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class CreateUserDto {

    @NotBlank
    @Email(message = "Email is invalid")
    private String email;

    @NotBlank
    @Size(min=10, max=11)
    @Pattern(regexp="(^$|[0-9]{10})")
    private String phone;

    @NotBlank
    @Length(min = 6, max = 30, message = "Password length must be more than 6 digits")
    private String password;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private Boolean isActive;

    private String urlAvatar;
}
