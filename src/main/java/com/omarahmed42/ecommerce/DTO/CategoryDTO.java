package com.omarahmed42.ecommerce.DTO;

import java.io.Serializable;

import lombok.Data;

@Data
public class CategoryDTO implements Serializable{
    private Integer id;
    private String name;
}
