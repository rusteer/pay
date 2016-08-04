package com.pay.admin.service;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.pay.admin.entity.PackageEntity;
import com.pay.admin.entity.PackageStatEntity;
import com.pay.admin.repository.PackageStatRepository;
import com.pay.admin.repository.mybatis.PackageStatRepositoryMyBatis;
import com.pay.admin.service.framework.AbstractService;

@Component
@Transactional(readOnly = true)
public class PackageStatService extends AbstractService<PackageStatEntity> {
    @Autowired
    private PackageStatRepository repository;
    @Autowired
    PackageService packageService;
    @Autowired
    private PackageStatRepositoryMyBatis myBatisRepository;
    public List<PackageStatEntity> getSumList(Map<String, Object> params) {
        return myBatisRepository.getSumList(params);
    }
    public PackageStatEntity load(long packageId, String statDate) {
        PackageStatEntity stat = repository.findByStatDateAndPackageId(statDate, packageId);
        return stat;
    }
    @Override
    protected PackageStatRepository getRepository() {
        return repository;
    }
    public List<PackageStatEntity> getChannelSumList(Map<String, Object> params) {
        return myBatisRepository.getChannelSumList(params);
    }
    public List<PackageStatEntity> getPackageSumList(Map<String, Object> params) {
        return myBatisRepository.getPackageSumList(params);
    }
    @Transactional(readOnly = false)
    public PackageStatEntity loadAndSave(PackageStatEntity entity) {
        PackageStatEntity dbData = this.load(entity.getPackageId(), entity.getStatDate());
        if (dbData != null) {
            entity.setId(dbData.getId());
        }
        PackageEntity pkg = this.packageService.get(entity.getPackageId());
        entity.setProductId(pkg.getProductId());
        entity.setChannelId(pkg.getChannelId());
        return save(entity);
    }
}
