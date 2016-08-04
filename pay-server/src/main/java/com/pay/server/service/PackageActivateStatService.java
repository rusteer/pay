package com.pay.server.service;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.pay.server.entity.PackageActivateStatEntity;
import com.pay.server.entity.PackageEntity;
import com.pay.server.repository.PackageActivateStatDao;
import com.pay.server.utils.FormatUtil;

@Component
@Transactional
public class PackageActivateStatService extends AbstractService<PackageActivateStatEntity> {
    @Autowired
    private PackageActivateStatDao dao;
    @Autowired
    SettingService settingService;
    @Override
    protected PackageActivateStatDao getDao() {
        return dao;
    }
    public PackageActivateStatEntity touch(PackageEntity pkg, String statDate) {
        PackageActivateStatEntity entity = dao.getByUniqeKeys(pkg.getId(), statDate);
        if (entity == null) {
            entity = new PackageActivateStatEntity();
            entity.setPackageId(pkg.getId());
            entity.setChannelId(pkg.getChannelId());
            entity.setProductId(pkg.getProductId());
            entity.setStatDate(statDate);
            entity = dao.save(entity);
        }
        return entity;
    }
    public void increaseActivateCount(PackageEntity pkg) {
        String statDate = FormatUtil.format(new Date());
        touch(pkg, statDate);
        dao.increaseActivateCount(pkg.getId(), statDate);
    }
}
