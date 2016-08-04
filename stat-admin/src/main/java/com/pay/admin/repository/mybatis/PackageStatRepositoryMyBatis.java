package com.pay.admin.repository.mybatis;
import java.util.List;
import java.util.Map;
import com.pay.admin.entity.PackageStatEntity;
import com.pay.admin.repository.framework.MyBatisRepository;

@MyBatisRepository
public interface PackageStatRepositoryMyBatis {
    List<PackageStatEntity> getSumList(Map<String, Object> params);

    List<PackageStatEntity> getListByDate(Map<String, Object> params);

    List<PackageStatEntity> getChannelSumList(Map<String, Object> params);

    List<PackageStatEntity> getPackageSumList(Map<String, Object> params);
}
