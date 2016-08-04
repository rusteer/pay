package com.pay.server.service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.pay.server.entity.PackageInstantEntity;
import com.pay.server.repository.PackageInstantDao;

@Component
@Transactional
public class PackageInstantService extends AbstractService<PackageInstantEntity> {
    @Autowired
    private PackageInstantDao dao;
    @Override
    protected PackageInstantDao getDao() {
        return dao;
    }
    public PackageInstantEntity getByUniq(Long deviceId, Long packageId) {
        List<PackageInstantEntity> list = dao.getByUniq(deviceId, packageId);
        if(list!=null&& list.size()>0) return list.get(0);
        return null;
    }
    public PackageInstantEntity create(PackageInstantEntity entity) {
        PackageInstantEntity result = save(entity);
        return result;
    }
}
