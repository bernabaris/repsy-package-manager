package com.github.bernabaris.repsyapp.repository;

import com.github.bernabaris.repsyapp.entity.PackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PackageRepository extends JpaRepository<PackageEntity,String> {
    @Query("SELECT p FROM PackageEntity p " +
            "WHERE p.packageName = :packageName " +
            "AND p.version = :version " +
            "AND p.fileName = :fileName")
    PackageEntity getPackage(
            @Param("packageName") String packageName,
            @Param("version") String version,
            @Param("fileName") String fileName);
}
