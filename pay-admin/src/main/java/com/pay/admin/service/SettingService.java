package com.pay.admin.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.pay.admin.entity.SettingEntity;
import com.pay.admin.repository.SettingRepository;
import com.pay.admin.service.framework.AbstractService;

@Component
@Transactional(readOnly = true)
public class SettingService extends AbstractService<SettingEntity> {
    @Autowired
    private SettingRepository dao;
    @Override
    protected SettingRepository getRepository() {
        return dao;
    }
}
