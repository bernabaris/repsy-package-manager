package com.github.bernabaris.repsyapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "packages")
public class PackageEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "package_name")
    private String packageName;

    @Column(name = "version")
    private String version;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "data")
    private byte[] data;

}
