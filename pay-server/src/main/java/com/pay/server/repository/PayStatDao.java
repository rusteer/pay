package com.pay.server.repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.pay.server.entity.PayStatEntity;

public interface PayStatDao extends PagingAndSortingRepository<PayStatEntity, Long>, JpaSpecificationExecutor<PayStatEntity> {
    @Query("from PayStatEntity a where a.packageInstantId=?1 and a.statDate=?2 ")
    PayStatEntity findByUniqueKeys(Long packageInstantId, String statDate);
    //
    @Modifying
    @Query("update PayStatEntity a set a.successCount=a.successCount+1,a.successEarning=a.successEarning + ?3 where a.packageInstantId=?1  and a.statDate=?2")
    void increaseSuccessCount(Long instantId, String date, int price);
    //
    @Modifying
    @Query("update PayStatEntity a set a.cancelCount=a.cancelCount+1 where a.packageInstantId=?1  and a.statDate=?2")
    void increaseCancelCount(Long instantId, String date);
    //
    @Modifying
    @Query("update PayStatEntity a set a.failCount=a.failCount+1 where a.packageInstantId=?1 and  a.statDate=?2")
    void increaseFailCount(Long instantId, String date);
}
