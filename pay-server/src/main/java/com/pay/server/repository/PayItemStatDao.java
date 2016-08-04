package com.pay.server.repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.pay.server.entity.PayItemStatEntity;

public interface PayItemStatDao extends PagingAndSortingRepository<PayItemStatEntity, Long>, JpaSpecificationExecutor<PayItemStatEntity> {
    @Query("from PayItemStatEntity a where a.packageInstantId=?1 and a.payIndex=?2 and a.statDate=?3 ")
    PayItemStatEntity findByUniqueKeys(Long packageInstantId, Integer payIndex, String statDate);
    //
    @Modifying
    @Query("update PayItemStatEntity a set a.successCount=a.successCount+1 where a.packageInstantId=?1 and a.payIndex=?2 and a.statDate=?3")
    void increaseSuccessCount(Long instantId, int payIndex, String date);
    //
    @Modifying
    @Query("update PayItemStatEntity a set a.cancelCount=a.cancelCount+1 where a.packageInstantId=?1 and a.payIndex=?2 and a.statDate=?3")
    void increaseCancelCount(Long instantId, int payIndex, String date);
    //
    @Modifying
    @Query("update PayItemStatEntity a set a.failCount=a.failCount+1 where a.packageInstantId=?1 and a.payIndex=?2 and a.statDate=?3")
    void increaseFailCount(Long instantId, int payIndex, String date);
}
