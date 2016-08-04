package com.pay.server.repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.pay.server.entity.PackageStatEntity;

public interface PackageStatDao extends PagingAndSortingRepository<PackageStatEntity, Long>, JpaSpecificationExecutor<PackageStatEntity> {
    @Query("from PackageStatEntity a where a.packageId=?1 and a.statDate=?2 ")
    PackageStatEntity getByUniqeKeys(Long id, String statDate);
    @Modifying
    @Query("update PackageStatEntity a set a.discountActivateCount=a.discountActivateCount+1 where a.packageId=?1  and a.statDate=?2")
    void increaseDiscountActivateCount(Long id, String statDate);
}
