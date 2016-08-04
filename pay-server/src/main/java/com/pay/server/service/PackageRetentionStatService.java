package com.pay.server.service;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.pay.server.entity.PackageEntity;
import com.pay.server.entity.PackageRetentionStatEntity;
import com.pay.server.repository.PackageRetentionStatDao;
import com.pay.server.utils.FormatUtil;

@Component
@Transactional
public class PackageRetentionStatService extends AbstractService<PackageRetentionStatEntity> {
    @Autowired
    private PackageRetentionStatDao dao;
    @Override
    protected PackageRetentionStatDao getDao() {
        return dao;
    }
    private void createStat(PackageEntity pkg, String createDate, String statDate,int retentionDays) {
        PackageRetentionStatEntity entity = dao.getByUniqeKeys(pkg.getId(), createDate, statDate);
        if (entity == null) {
            entity = new PackageRetentionStatEntity();
            entity.setPackageId(pkg.getId());
            entity.setCreateDate(createDate);
            entity.setStatDate(statDate);
            entity.setRetentionDays(retentionDays);
            //
            entity.setChannelId(pkg.getChannelId());
            entity.setProductId(pkg.getProductId());
            entity = dao.save(entity);
        }
    }
    public void increaseRetentionCount(PackageEntity pkg, String createDate, int retentionDays) {
        String statDate = FormatUtil.format(new Date());
        createStat(pkg, createDate, statDate,retentionDays);
        dao.increaseRetentionCount(pkg.getId(), createDate, statDate);
    }
}
