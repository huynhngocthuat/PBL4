package com.edu.bkdn.dtos.Attachment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class GetAttachmentDto {

    @NotEmpty
    private Long id;

    @NotEmpty
    private byte[] data;

    @NotEmpty
    private String fileName;

    @NotEmpty
    private String fileType;

    @NotEmpty
    private String createAt;

    private String updateAt;

    @NotEmpty
    private String messageId;
}
