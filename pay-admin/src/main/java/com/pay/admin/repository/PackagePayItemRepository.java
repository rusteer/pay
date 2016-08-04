package com.pay.admin.repository;
import java.util.List;
import com.pay.admin.entity.PackagePayItemEntity;
import com.pay.admin.repository.framework.MyJpaRepository;

public interface PackagePayItemRepository extends MyJpaRepository<PackagePayItemEntity> {

    List<PackagePayItemEntity> findByPackageId(Long packageId);}
