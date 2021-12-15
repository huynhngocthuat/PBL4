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
public class CreateAttachmentDto {

    @NotEmpty
    private byte[] data;

    @NotEmpty
    private String fileName;

    @NotEmpty
    private String fileType;

}