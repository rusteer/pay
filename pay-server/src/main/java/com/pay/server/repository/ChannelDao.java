package com.pay.server.repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.pay.server.entity.ChannelEntity;

public interface ChannelDao extends PagingAndSortingRepository<ChannelEntity, Long>, JpaSpecificationExecutor<ChannelEntity> {
    @Query("from ChannelEntity a where a.shortName=?1  ")
    ChannelEntity getByShortName(String shortName);
}
