package com.omarahmed42.ecommerce.DTO;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import lombok.Data;

@Data
public class ReviewCommentResponse implements Serializable {
    private UUID id;
    private String title;
    private String content;
    private Instant createdAt;
}
