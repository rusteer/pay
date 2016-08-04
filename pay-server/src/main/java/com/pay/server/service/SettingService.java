package com.pay.server.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.pay.server.entity.SettingEntity;
import com.pay.server.repository.SettingDao;

@Component
@Transactional
public class SettingService extends AbstractService<SettingEntity> {
    @Autowired
    private SettingDao dao;
    @Override
    protected SettingDao getDao() {
        return dao;
    }
    public SettingEntity get() {
        return dao.get();
    }
    
   
}
