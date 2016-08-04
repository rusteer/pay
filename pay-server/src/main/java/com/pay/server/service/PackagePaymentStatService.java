package com.pay.server.service;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.pay.server.entity.PackageEntity;
import com.pay.server.entity.PackagePaymentStatEntity;
import com.pay.server.repository.PackagePaymentStatDao;
import com.pay.server.utils.FormatUtil;

@Component
@Transactional
public class PackagePaymentStatService extends AbstractService<PackagePaymentStatEntity> {
    @Autowired
    private PackagePaymentStatDao dao;
    @Autowired
    SettingService settingService;
    @Override
    protected PackagePaymentStatDao getDao() {
        return dao;
    }
    private void createStat(PackageEntity pkg, Long sdkId, String statDate) {
        PackagePaymentStatEntity entity = dao.getByUniqeKeys(pkg.getId(), sdkId, statDate);
        if (entity == null) {
            entity = new PackagePaymentStatEntity();
            entity.setPackageId(pkg.getId());
            entity.setSdkId(sdkId);
            entity.setStatDate(statDate);
            entity.setChannelId(pkg.getChannelId());
            entity.setProductId(pkg.getProductId());
            entity = dao.save(entity);
        }
    }
    public void increasePayCount(PackageEntity pkg, Long sdkId, int price) {
        String statDate = FormatUtil.format(new Date());
        createStat(pkg, sdkId, statDate);
        dao.increasePayCount(pkg.getId(), sdkId, statDate, price);
    }
}
