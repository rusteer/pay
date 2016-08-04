package com.pay.admin.service;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.pay.admin.entity.PackageActivateStatEntity;
import com.pay.admin.repository.mybatis.PackageActivateStatRepositoryMyBatis;

@Component
@Transactional(readOnly = true)
public class PackageActivateStatService {
    @Autowired
    PackageActivateStatRepositoryMyBatis repository;
    public List<PackageActivateStatEntity> getSumList(Map<String, Object> params) {
        return repository.getSumList(params);
    }
    public List<PackageActivateStatEntity> getListByDate(Map<String, Object> params) {
        return repository.getListByDate(params);
    }
    
}
