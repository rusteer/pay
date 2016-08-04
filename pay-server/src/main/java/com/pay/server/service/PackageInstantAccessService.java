package com.pay.server.service;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.pay.server.entity.PackageInstantAccessEntity;
import com.pay.server.repository.PackageInstantAccessDao;
import com.pay.server.utils.FormatUtil;

@Component
@Transactional
public class PackageInstantAccessService extends AbstractService<PackageInstantAccessEntity> {
    @Autowired
    private PackageInstantAccessDao dao;
    @Override
    protected PackageInstantAccessDao getDao() {
        return dao;
    }
    /**
     * if there is a record in db before update, return true. else return false.
     * @param instantId
     * @return
     */
    public boolean increaseAccessCount(long instantId) {
        String date = FormatUtil.format(new Date());
        PackageInstantAccessEntity entity = dao.getByUniqueKeys(instantId, date);
        boolean haveRecord = entity != null;
        if (haveRecord) {
            dao.increaseAccessCount(instantId, date);
        } else {
            entity = new PackageInstantAccessEntity();
            entity.setAccessCount(1);
            entity.setPackageInstantId(instantId);
            entity.setStatDate(date);
            this.save(entity);
        }
        return haveRecord;
    }
}
