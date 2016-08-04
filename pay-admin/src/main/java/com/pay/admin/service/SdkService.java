package com.pay.admin.service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.pay.admin.entity.SdkEntity;
import com.pay.admin.repository.SdkRepository;
import com.pay.admin.service.framework.AbstractService;

@Component
@Transactional(readOnly = true)
public class SdkService extends AbstractService<SdkEntity> {
    @Autowired
    private SdkRepository dao;
    @Override
    protected SdkRepository getRepository() {
        return dao;
    }
    public Map<String, SdkEntity> getMapByName() {
        Map<String, SdkEntity> sdkMap = new HashMap<String, SdkEntity>();
        for (SdkEntity sdk : this.getAll()) {
            sdkMap.put(sdk.getName(), sdk);
        }
        return sdkMap;
    }
    public List<SdkEntity> getListOrderByHot() {
        return dao.getListOrderByHot();
    }
}
