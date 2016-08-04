package com.pay.admin.service;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.pay.admin.entity.PackageEntity;
import com.pay.admin.entity.PackagePaymentStatEntity;
import com.pay.admin.repository.PackagePaymentStatRepository;
import com.pay.admin.repository.mybatis.PackagePaymentStatRepositoryMyBatis;
import com.pay.admin.service.framework.AbstractService;

@Component
@Transactional(readOnly = true)
public class PackagePaymentStatService extends AbstractService<PackagePaymentStatEntity> {
    @Autowired
    private PackageService packageService;
    @Autowired
    private PackagePaymentStatRepository repository;
    @Autowired
    private PackagePaymentStatRepositoryMyBatis myBatisRepository;
    public List<PackagePaymentStatEntity> getSumList(Map<String, Object> params) {
        return myBatisRepository.getSumList(params);
    }
    public List<PackagePaymentStatEntity> getListByDate(Map<String, Object> params) {
        return myBatisRepository.getListByDate(params);
    }
    @Override
    protected PackagePaymentStatRepository getRepository() {
        return repository;
    }
    public List<PackagePaymentStatEntity> findByPackageIdAndStatDate(Long packageId, String statDate) {
        return this.repository.findByPackageIdAndStatDate(packageId, statDate);
    }
    @Transactional(readOnly = false)
    public void updateSyncEarning(Long packageId, String statDate, long sdkId, int syncEarning) {
        PackagePaymentStatEntity entity = this.repository.findByPackageIdAndStatDateAndSdkId(packageId, statDate, sdkId);
        if (entity == null) {
            entity = new PackagePaymentStatEntity();
            entity.setPackageId(packageId);
            entity.setStatDate(statDate);
            entity.setSdkId(sdkId);
            PackageEntity pkg = packageService.get(packageId);
            if (pkg != null) {
                entity.setChannelId(pkg.getChannelId());
                entity.setProductId(pkg.getProductId());
            }
        }
        entity.setSyncEarning(syncEarning);
        this.save(entity);
    }
}
