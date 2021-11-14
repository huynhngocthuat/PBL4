package com.edu.bkdn.dtos.User;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class GetUserDto {
    @NotEmpty
    private Long id;

    @NotEmpty
    @Email
    private String email;

    @NotBlank
    @Size(min=10, max=11)
    @Pattern(regexp="(^$|[0-9]{10})")
    private String phone;

    @NotBlank
    private String firstName;

    @NotBlank
    private String surname;

    private Boolean isActive;

    private String urlAvatar;
}
