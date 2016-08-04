package com.pay.server.repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.pay.server.entity.SettingEntity;

public interface SettingDao extends PagingAndSortingRepository<SettingEntity, Long>, JpaSpecificationExecutor<SettingEntity> {
    @Query("from SettingEntity a where a.id=1  ")
    SettingEntity get();
}
