package com.edu.bkdn.dtos.Contact;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class PendingContactDto {
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
    private String lastName;

    @NotNull
    private String urlAvatar;

    private Boolean isAccepted;

    @NotNull
    private Long requestSenderId;

    private String invitationMessage;
}
