package com.github.bernabaris.repsyapp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="packages")
public class PackageEntity{
    @Id
    private String id;
    private String packageName;
    private String version;
    private String packagePath;
    private String metaPath;

}
