package com.pay.server.repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.pay.server.entity.SyncEntity;

public interface SyncDao extends PagingAndSortingRepository<SyncEntity, Long>, JpaSpecificationExecutor<SyncEntity> {
    @Query("from SyncEntity a where a.linkId=?1  ")
    SyncEntity getByLinkId(String linkId);
}
