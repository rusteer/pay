package com.pay.server.repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.pay.server.entity.PackageInstantAccessEntity;

public interface PackageInstantAccessDao extends PagingAndSortingRepository<PackageInstantAccessEntity, Long>, JpaSpecificationExecutor<PackageInstantAccessEntity> {
    @Query("from PackageInstantAccessEntity a where a.packageInstantId=?1 and a.statDate=?2 ")
    PackageInstantAccessEntity getByUniqueKeys(long instantId, String date);
    //
    @Modifying
    @Query("update PackageInstantAccessEntity a set a.accessCount=a.accessCount+1 where a.packageInstantId=?1  and a.statDate=?2")
    void increaseAccessCount(long instantId, String date);
}
