package com.pay.server.service;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.pay.server.entity.PackageEntity;
import com.pay.server.entity.PackageInstantEntity;
import com.pay.server.entity.PayItemStatEntity;
import com.pay.server.repository.PayItemStatDao;
import com.pay.server.utils.FormatUtil;

@Component
@Transactional
public class PayItemStatService extends AbstractService<PayItemStatEntity> {
    @Autowired
    private PayItemStatDao dao;
    @Autowired
    private PackageInstantService packageInstantService;
    @Autowired
    private PackageService packageService;
    @Override
    protected PayItemStatDao getDao() {
        return dao;
    }
    public void increaseStatCount(Long instantId, int payIndex, String payName, int price, int result) {
        String date = FormatUtil.format(new Date());
        PayItemStatEntity entity = this.dao.findByUniqueKeys(instantId, payIndex, date);
        if (entity == null) {
            PackageInstantEntity instant = packageInstantService.get(instantId);
            PackageEntity pkg = packageService.get(instant.getPackageId());
            entity = new PayItemStatEntity();
            entity.setChannelId(pkg.getChannelId());
            entity.setDeviceId(instant.getDeviceId());
            entity.setPackageId(pkg.getId());
            entity.setPackageInstantId(instantId);
            entity.setPayIndex(payIndex);
            entity.setPayName(payName);
            entity.setPrice(price);
            entity.setProductId(pkg.getProductId());
            entity.setStatDate(date);
            switch (result) {
                case 0://success
                    entity.setSuccessCount(1);
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
                    this.dao.increaseSuccessCount(instantId, payIndex, date);
                    break;
                case 1://cancel
                    this.dao.increaseCancelCount(instantId, payIndex, date);
                    break;
                case 2://fail
                    this.dao.increaseFailCount(instantId, payIndex, date);
                    break;
            }
        }
    }
}
