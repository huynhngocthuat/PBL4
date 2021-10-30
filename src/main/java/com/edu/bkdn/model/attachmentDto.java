package com.edu.bkdn.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class attachmentDto {
    @NotEmpty
    private long attachmentId;
    @NotEmpty
    private String thumbUrl;
    @NotEmpty
    private String fileUrl;
    @NotEmpty
    private Date createAt;
    @NotEmpty
    private Date updateAt;
    @NotEmpty
    private String messageId;
}
