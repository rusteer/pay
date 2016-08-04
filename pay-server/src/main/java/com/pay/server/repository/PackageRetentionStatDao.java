package com.pay.server.repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.pay.server.entity.PackageRetentionStatEntity;

public interface PackageRetentionStatDao extends PagingAndSortingRepository<PackageRetentionStatEntity, Long>, JpaSpecificationExecutor<PackageRetentionStatEntity> {
    /**
     * getByUniqeKeys
     * @param packageId
     * @param retentionDays
     * @param statDate
     * @return
     */
    @Query("from PackageRetentionStatEntity a where a.packageId=?1 and a.createDate=?2 and a.statDate=?3 ")
    PackageRetentionStatEntity getByUniqeKeys(Long packageId, String createDate, String statDate);
    
    /**
     * increaseRetentionCount
     * @param packageId
     * @param retentionDays
     * @param statDate
     */
    @Modifying
    @Query("update PackageRetentionStatEntity a set a.retentionCount=a.retentionCount+1  where a.packageId=?1 and a.createDate=?2 and a.statDate=?3  ")
    void increaseRetentionCount(Long packageId, String createDate, String statDate);
    
    
}
