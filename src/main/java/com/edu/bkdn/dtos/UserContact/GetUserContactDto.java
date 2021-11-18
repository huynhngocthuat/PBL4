package com.edu.bkdn.dtos.UserContact;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class GetUserContactDto {
    @NotNull
    private Long userId;
    @NotEmpty
    private String userFirstName;
    @NotEmpty
    private String userLastName;
    @NotEmpty
    private String userUrlAvatar;
    @NotNull
    private Long contactId;
    @NotEmpty
    private String contactFirstName;
    @NotEmpty
    private String contactLastName;
    @NotEmpty
    private String contactUrlAvatar;

}
