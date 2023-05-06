package com.omarahmed42.ecommerce.DTO;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;

import com.omarahmed42.ecommerce.enums.ProductSort;

import lombok.Data;

@Data
public class PageResponse<T> implements Serializable {
    private List<T> elements;
    private int elementsPerPage;
    private int currentPage;
    private int currentSize;
    private long totalElements;
    private int totalPages;
    private String sortOrder;

    public PageResponse(Page<?> page, List<T> elements) {
        setPage(page, elements);
    }

    public PageResponse(Page<?> page, List<T> elements, ProductSort productSort) {
        setPage(page, elements);
        this.sortOrder = productSort.getValue();
    }

    public void setPage(Page<?> page, List<T> elements) {
        this.elements = elements;
        this.elementsPerPage = page.getNumberOfElements();
        this.currentPage = page.getNumber() + 1;
        this.currentPage = page.getSize();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        sortOrder = page.getSort().toString();
    }
}
