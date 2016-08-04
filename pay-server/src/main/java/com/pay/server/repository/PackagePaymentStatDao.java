package com.pay.server.repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.pay.server.entity.PackagePaymentStatEntity;

public interface PackagePaymentStatDao extends PagingAndSortingRepository<PackagePaymentStatEntity, Long>, JpaSpecificationExecutor<PackagePaymentStatEntity> {
    /**
     * getByUniqeKeys
     * @param packageId
     * @param sdkId
     * @param statDate
     * @return
     */
    @Query("from PackagePaymentStatEntity a where a.packageId=?1 and a.sdkId=?2 and a.statDate=?3 ")
    PackagePaymentStatEntity getByUniqeKeys(Long packageId, Long sdkId, String statDate);
    /**
     * increasePayCount
     * @param packageId
     * @param sdkId
     * @param statDate
     * @param price
     */
    @Modifying
    @Query("update PackagePaymentStatEntity a set a.payCount=a.payCount+1,a.payEarning=a.payEarning + ?4 where  a.packageId=?1 and a.sdkId=?2 and a.statDate=?3 ")
    void increasePayCount(long packageId, Long sdkId, String statDate, int price);
}
