package com.github.bernabaris.repsyapp.repository;

import com.github.bernabaris.repsyapp.entity.PackageEntity;
import com.github.bernabaris.repsyapp.entity.StorageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PackageRepository extends JpaRepository<PackageEntity,String> {
    @Query("SELECT p FROM PackageEntity p " +
            "WHERE p.packageName = :packageName " +
            "AND p.version = :version " +
            "AND p.fileName = :fileName " +
            "AND p.storageType = :storageType")
    PackageEntity getPackage(
            @Param("packageName") String packageName,
            @Param("version") String version,
            @Param("fileName") String fileName,
            @Param("storageType") StorageType storageType);
}
