package com.pay.admin.repository.mybatis;
import java.util.List;
import java.util.Map;
import com.pay.admin.entity.PackagePaymentStatEntity;
import com.pay.admin.repository.framework.MyBatisRepository;

@MyBatisRepository
public interface PackagePaymentStatRepositoryMyBatis {
    List<PackagePaymentStatEntity> getSumList(Map<String, Object> params);

    List<PackagePaymentStatEntity> getListByDate(Map<String, Object> params);
}
