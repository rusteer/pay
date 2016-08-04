package com.pay.admin.repository;
import com.pay.admin.entity.PackageStatEntity;
import com.pay.admin.repository.framework.MyJpaRepository;

public interface PackageStatRepository extends MyJpaRepository<PackageStatEntity> {
    PackageStatEntity findByStatDateAndPackageId(String statDate, long packageId);
}
