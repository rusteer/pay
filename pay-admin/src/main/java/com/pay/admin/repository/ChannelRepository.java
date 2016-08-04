package com.pay.admin.repository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import com.pay.admin.entity.ChannelEntity;
import com.pay.admin.repository.framework.MyJpaRepository;

public interface ChannelRepository extends MyJpaRepository<ChannelEntity> {

    @Query("from ChannelEntity a order by a.hot desc  ")
    List<ChannelEntity> getListOrderByHot();

    ChannelEntity findByLoginName(String name);
}
