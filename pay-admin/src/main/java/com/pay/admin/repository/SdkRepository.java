package com.pay.admin.repository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import com.pay.admin.entity.SdkEntity;
import com.pay.admin.repository.framework.MyJpaRepository;

public interface SdkRepository extends MyJpaRepository<SdkEntity> {
    @Query("from SdkEntity a order by a.hot desc  ")
    List<SdkEntity> getListOrderByHot();
}
