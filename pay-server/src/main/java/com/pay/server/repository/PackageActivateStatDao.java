package com.pay.server.repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.pay.server.entity.PackageActivateStatEntity;

public interface PackageActivateStatDao extends PagingAndSortingRepository<PackageActivateStatEntity, Long>, JpaSpecificationExecutor<PackageActivateStatEntity> {
    /**
     * getByUniqeKeys
     * @param packageId
     * @param statDate
     * @return
     */
    @Query("from PackageActivateStatEntity a where a.packageId=?1 and a.statDate=?2 ")
    PackageActivateStatEntity getByUniqeKeys(Long packageId, String statDate);
    /**
     * increaseActivateCount
     * @param packageId
     * @param statDate
     */
    @Modifying
    @Query("update PackageActivateStatEntity a set a.activateCount=a.activateCount+1 where a.packageId=?1  and a.statDate=?2")
    void increaseActivateCount(long packageId, String statDate);
}
