package com.pay.server.service;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.pay.server.entity.PackageEntity;
import com.pay.server.entity.PackagePaymentDetailStatEntity;
import com.pay.server.repository.PackagePaymentDetailStatDao;
import com.pay.server.utils.FormatUtil;

@Component
@Transactional
public class PackagePaymentDetailStatService extends AbstractService<PackagePaymentDetailStatEntity> {
    @Autowired
    private PackagePaymentDetailStatDao dao;
    @Override
    protected PackagePaymentDetailStatDao getDao() {
        return dao;
    }
   
    /**
     * 
     * @param pkg
     * @param retentionDays
     * @param carrierOperator
     * @param sdkId
     * @param payIndex
     * @param statDate
     */
    private void createStat(PackageEntity pkg, int retentionDays, int carrierOperator, Long sdkId, int payIndex, String statDate) {
        PackagePaymentDetailStatEntity entity = dao.getByUniqeKeys(pkg.getId(), retentionDays, carrierOperator, sdkId, payIndex, statDate);
        if (entity == null) {
            entity = new PackagePaymentDetailStatEntity();
            entity.setPackageId(pkg.getId());
            entity.setChannelId(pkg.getChannelId());
            entity.setProductId(pkg.getProductId());
            entity.setRetentionDays(retentionDays);
            entity.setCarrierOperator(carrierOperator);
            entity.setSdkId(sdkId);
            entity.setPayIndex(payIndex);
            entity.setStatDate(statDate);
            //
            entity.setSuccessCount(0);
            entity.setCancelCount(0);
            entity.setFailureCount(0);
            entity = dao.save(entity);
        }
    }
    public static final int PAY_SUCCESS = 0;
    public static final int PAY_CANCEL = 1;
    public static final int PAY_FAILED = 2;
    public void increaseCount(PackageEntity pkg, int retentionDays, int carrierOperator, Long sdkId, int payIndex, int result) {
        String statDate = FormatUtil.format(new Date());
        this.createStat(pkg, retentionDays, carrierOperator, sdkId, payIndex, statDate);
        switch (result) {
            case PAY_SUCCESS:
                dao.increaseSuccessCount(pkg.getId(), retentionDays, carrierOperator, sdkId, payIndex, statDate);
                break;
            case PAY_CANCEL:
                dao.increaseCancelCount(pkg.getId(), retentionDays, carrierOperator, sdkId, payIndex, statDate);
                break;
            case PAY_FAILED:
                dao.increaseFailureCount(pkg.getId(), retentionDays, carrierOperator, sdkId, payIndex, statDate);
                break;
        }
    }
}
