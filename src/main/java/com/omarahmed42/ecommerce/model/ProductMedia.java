package com.omarahmed42.ecommerce.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Audited
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class ProductMedia extends Attachment {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id", columnDefinition = "BINARY(16)")
    private Product product;

    public ProductMedia(String filename, Long size, String path) {
        this.filename = filename;
        this.size = size;
        this.path = path;
    }
}
