package com.pay.server.service;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.pay.server.entity.PackageEntity;
import com.pay.server.entity.PackageInstantEntity;
import com.pay.server.entity.PayStatEntity;
import com.pay.server.repository.PayStatDao;
import com.pay.server.utils.FormatUtil;

@Component
@Transactional
public class PayStatService extends AbstractService<PayStatEntity> {
    @Autowired
    private PayStatDao dao;
    @Autowired
    private PackageInstantService packageInstantService;
    @Autowired
    private PackageService packageService;
    @Override
    protected PayStatDao getDao() {
        return dao;
    }
    public void increaseStatCount(Long instantId, int price, int result) {
        String date = FormatUtil.format(new Date());
        PayStatEntity entity = this.dao.findByUniqueKeys(instantId, date);
        if (entity == null) {
            PackageInstantEntity instant = packageInstantService.get(instantId);
            PackageEntity pkg = packageService.get(instant.getPackageId());
            entity = new PayStatEntity();
            entity.setChannelId(pkg.getChannelId());
            entity.setDeviceId(instant.getDeviceId());
            entity.setDiscountSuccessCount(0);
            entity.setPackageId(pkg.getId());
            entity.setPackageInstantId(instantId);
            entity.setProductId(pkg.getProductId());
            entity.setStatDate(date);
            entity.setSyncSuccessCount(0);
            switch (result) {
                case 0://success
                    entity.setSuccessCount(1);
                    entity.setSuccessEarning(price);
                    break;
                case 1://cancel
                    entity.setCancelCount(1);
                    break;
                case 2://fail
                    entity.setFailCount(1);
                    break;
            }
            this.save(entity);
        } else {
            switch (result) {
                case 0://success
                    this.dao.increaseSuccessCount(instantId, date, price);
                    break;
                case 1://cancel
                    this.dao.increaseCancelCount(instantId, date);
                    break;
                case 2://fail
                    this.dao.increaseFailCount(instantId, date);
                    break;
            }
        }
    }
}
