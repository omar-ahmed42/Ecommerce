package com.omarahmed42.ecommerce.model;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@EqualsAndHashCode
@Getter
@Setter
public class Attachment implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, columnDefinition = "BINARY(16)")
    @Access(AccessType.PROPERTY)
    protected UUID id;

    @Column(name = "filename", nullable = false)
    protected String filename;

    @Column(name = "size")
    protected Long size;

    @Column(name = "path")
    protected String path;
}