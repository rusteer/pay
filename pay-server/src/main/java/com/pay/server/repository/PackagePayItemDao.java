package com.pay.server.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.pay.server.entity.PackagePayItemEntity;

public interface PackagePayItemDao extends PagingAndSortingRepository<PackagePayItemEntity, Long>, JpaSpecificationExecutor<PackagePayItemEntity> {

    List<PackagePayItemEntity> findByPackageId(Long packageId);}
