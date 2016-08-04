package com.pay.admin.service;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.pay.admin.bean.PayPointStat;
import com.pay.admin.repository.mybatis.PayReportRepositoryMyBatis;

@Component
@Transactional(readOnly = true)
public class PayReportService {
    @Autowired
    private PayReportRepositoryMyBatis myBatisRepository;
    public List<PayPointStat> getSumList(Map<String, Object> params) {
        return myBatisRepository.getSumList(params);
    }
}
