package com.github.bernabaris.repsyapp.repository;

import com.github.bernabaris.repsyapp.entity.PackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PackageRepository extends JpaRepository<PackageEntity,String> {
}
