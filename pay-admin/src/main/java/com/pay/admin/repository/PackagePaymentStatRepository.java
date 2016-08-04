package com.pay.admin.repository;
import java.util.List;
import com.pay.admin.entity.PackagePaymentStatEntity;
import com.pay.admin.repository.framework.MyJpaRepository;

public interface PackagePaymentStatRepository extends MyJpaRepository<PackagePaymentStatEntity> {

    List<PackagePaymentStatEntity> findByPackageIdAndStatDate(Long packageId, String statDate);

    PackagePaymentStatEntity findByPackageIdAndStatDateAndSdkId(Long packageId, String statDate, long sdkId);}
