package com.pay.server.repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.pay.server.entity.SdkEntity;

public interface SdkDao extends PagingAndSortingRepository<SdkEntity, Long>, JpaSpecificationExecutor<SdkEntity> {
    @Query("from SdkEntity a where a.encodedName=?1  ")
    SdkEntity getByEncodedName(String encodedName);
}
