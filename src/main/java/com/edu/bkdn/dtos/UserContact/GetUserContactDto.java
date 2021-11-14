package com.edu.bkdn.dtos.UserContact;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class GetUserContactDto {
    @NotNull
    private Long userId;
    @NotNull
    private Long contactId;
}
