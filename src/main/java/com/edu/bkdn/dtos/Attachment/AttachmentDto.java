package com.edu.bkdn.dtos.Attachment;

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
public class AttachmentDto {
    @NotEmpty
    private Long attachmentId;
    @NotEmpty
    private String thumbUrl;
    @NotEmpty
    private String fileUrl;
    @NotEmpty
    private Timestamp createAt;
    @NotEmpty
    private Timestamp updateAt;
    @NotEmpty
    private String messageId;
}
