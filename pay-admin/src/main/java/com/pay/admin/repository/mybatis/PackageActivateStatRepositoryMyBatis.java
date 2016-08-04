package com.pay.admin.repository.mybatis;
import java.util.List;
import java.util.Map;
import com.pay.admin.entity.PackageActivateStatEntity;
import com.pay.admin.repository.framework.MyBatisRepository;

@MyBatisRepository
public interface PackageActivateStatRepositoryMyBatis {
    List<PackageActivateStatEntity> getSumList(Map<String, Object> params);

    List<PackageActivateStatEntity> getListByDate(Map<String, Object> params);
}
