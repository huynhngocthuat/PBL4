package com.edu.bkdn.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class messageDto {
    @NotEmpty
    private long messageId;
    @NotEmpty
    private String message;
    @NotEmpty
    private Date createAt;
    @NotEmpty
    private Date updateAt;
    @NotEmpty
    private long senderId;
    @NotEmpty
    private long conversationId;
}
