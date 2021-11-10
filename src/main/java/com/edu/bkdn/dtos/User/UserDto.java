package com.edu.bkdn.dtos.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.sql.Timestamp;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class UserDto {
    @NotEmpty
    private Long id;
    @NotEmpty
    private String email;
    @NotEmpty
    private String phone;
    @NotEmpty
    private String password;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String surname;
    @NotEmpty
    private Boolean isActive;
    @NotEmpty
    private Timestamp createAt;
    @NotEmpty
    private Timestamp updateAt;
}