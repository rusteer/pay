package com.pay.server.service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.pay.server.entity.PackagePayItemEntity;
import com.pay.server.repository.PackagePayItemDao;

@Component
@Transactional
public class PackagePayItemService extends AbstractService<PackagePayItemEntity> {
    @Autowired
    private PackagePayItemDao dao;
    @Override
    protected PackagePayItemDao getDao() {
        return dao;
    }
    public List<PackagePayItemEntity> findByPackageId(Long packageId) {
       return dao.findByPackageId(packageId);
        
    }
}
