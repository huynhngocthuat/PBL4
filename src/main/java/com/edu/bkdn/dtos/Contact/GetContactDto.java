package com.edu.bkdn.dtos.Contact;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class GetContactDto {
    @NotEmpty
    private Long id;

    @NotEmpty
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank
    @Size(min=10, max=11)
    @Pattern(regexp="(^$|[0-9]{10})")
    private String phone;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String surname;

    private Boolean isActive;

    @NotNull
    private String urlAvatar;
}
