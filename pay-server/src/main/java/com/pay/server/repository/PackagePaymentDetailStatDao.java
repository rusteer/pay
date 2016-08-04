package com.pay.server.repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.pay.server.entity.PackagePaymentDetailStatEntity;

public interface PackagePaymentDetailStatDao extends PagingAndSortingRepository<PackagePaymentDetailStatEntity, Long>, JpaSpecificationExecutor<PackagePaymentDetailStatEntity> {
    /**
     * getByUniqeKeys
     * @param packageId
     * @param retentionDays
     * @param carrierOperator
     * @param sdkId
     * @param payIndex
     * @param statDate
     * @return
     */
    @Query("from PackagePaymentDetailStatEntity a where a.packageId=?1  and retentionDays=?2 and carrierOperator=?3 and sdkId=?4 and payIndex=?5 and a.statDate=?6 ")
    PackagePaymentDetailStatEntity getByUniqeKeys(Long packageId, int retentionDays, int carrierOperator, Long sdkId, int payIndex, String statDate);
    /**
     * increaseSuccessCount
     * @param packageId
     * @param retentionDays
     * @param carrierOperator
     * @param sdkId
     * @param payIndex
     * @param statDate
     */
    @Modifying
    @Query("update PackagePaymentDetailStatEntity a set a.successCount=a.successCount+1 where a.packageId=?1  and retentionDays=?2 and carrierOperator=?3 and sdkId=?4 and payIndex=?5 and a.statDate=?6 ")
    void increaseSuccessCount(Long packageId, int retentionDays, int carrierOperator, Long sdkId, int payIndex, String statDate);
    /**
     * increaseCancelCount
     * @param packageId
     * @param retentionDays
     * @param carrierOperator
     * @param sdkId
     * @param payIndex
     * @param statDate
     */
    @Modifying
    @Query("update PackagePaymentDetailStatEntity a set a.cancelCount=a.cancelCount+1 where a.packageId=?1  and retentionDays=?2 and carrierOperator=?3 and sdkId=?4 and payIndex=?5 and a.statDate=?6 ")
    void increaseCancelCount(Long packageId, int retentionDays, int carrierOperator, Long sdkId, int payIndex, String statDate);
    /**
     * increaseCancelCount
     * @param packageId
     * @param retentionDays
     * @param carrierOperator
     * @param sdkId
     * @param payIndex
     * @param statDate
     */
    @Modifying
    @Query("update PackagePaymentDetailStatEntity a set a.failureCount=a.failureCount+1 where a.packageId=?1  and retentionDays=?2 and carrierOperator=?3 and sdkId=?4 and payIndex=?5 and a.statDate=?6 ")
    void increaseFailureCount(Long packageId, int retentionDays, int carrierOperator, Long sdkId, int payIndex, String statDate);
}
