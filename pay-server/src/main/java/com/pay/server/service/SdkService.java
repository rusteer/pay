package com.pay.server.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.pay.server.entity.SdkEntity;
import com.pay.server.repository.SdkDao;

@Component
@Transactional
public class SdkService extends AbstractService<SdkEntity> {
    @Autowired
    private SdkDao dao;
    @Override
    protected SdkDao getDao() {
        return dao;
    }
    public SdkEntity getByEncodedName(String encodedSdk) {
       return dao.getByEncodedName(encodedSdk);
    }
}
