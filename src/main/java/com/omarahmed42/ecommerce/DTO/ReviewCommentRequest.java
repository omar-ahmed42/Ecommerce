package com.omarahmed42.ecommerce.DTO;

import java.io.Serializable;

import lombok.Data;

@Data
public class ReviewCommentRequest implements Serializable {
    private String title;
    private String content;
}
