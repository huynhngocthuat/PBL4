package com.edu.bkdn.dtos.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class UpdateUserDto {
    @NotEmpty
    private String phone;
    @NotEmpty
    private String email;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    private String urlAvatar;
}
