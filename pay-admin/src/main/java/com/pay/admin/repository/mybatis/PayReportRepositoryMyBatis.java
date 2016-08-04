package com.pay.admin.repository.mybatis;
import java.util.List;
import java.util.Map;
import com.pay.admin.bean.PayPointStat;
import com.pay.admin.repository.framework.MyBatisRepository;

@MyBatisRepository
public interface PayReportRepositoryMyBatis {
    List<PayPointStat> getSumList(Map<String, Object> params);
}
