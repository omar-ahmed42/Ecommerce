package com.omarahmed42.ecommerce.DTO;

import java.io.Serializable;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttachmentInfo implements Serializable {
    private UUID id;
    private String filename;
    private Long size;
    private String path;

    public AttachmentInfo(String filename, Long size, String path) {
        this.filename = filename;
        this.size = size;
        this.path = path;
    }
}
