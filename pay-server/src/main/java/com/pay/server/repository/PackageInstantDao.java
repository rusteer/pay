package com.pay.server.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.pay.server.entity.PackageInstantEntity;

public interface PackageInstantDao extends PagingAndSortingRepository<PackageInstantEntity, Long>, JpaSpecificationExecutor<PackageInstantEntity> {
 
    @Query("  from PackageInstantEntity a where a.deviceId=?1 and a.packageId=?2 ")
    List<PackageInstantEntity> getByUniq(Long deviceId, Long packageId);
}
