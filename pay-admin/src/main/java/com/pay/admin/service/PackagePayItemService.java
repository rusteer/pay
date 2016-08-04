package com.pay.admin.service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.pay.admin.entity.PackagePayItemEntity;
import com.pay.admin.repository.PackagePayItemRepository;
import com.pay.admin.service.framework.AbstractService;

@Component
@Transactional(readOnly = true)
public class PackagePayItemService extends AbstractService<PackagePayItemEntity> {
    @Autowired
    private PackagePayItemRepository dao;
    @Override
    protected PackagePayItemRepository getRepository() {
        return dao;
    }
    public List<PackagePayItemEntity> findByPackageId(Long packageId) {
        return dao.findByPackageId(packageId);
    }
    @Transactional(readOnly = false)
    public void saveOrUpdate(long packageId, List<PackagePayItemEntity> packageItems) {
        Map<Integer, PackagePayItemEntity> dbMap = toMap(this.findByPackageId(packageId));
        Map<Integer, PackagePayItemEntity> requestMap = toMap(packageItems);
        for (PackagePayItemEntity dbItem : dbMap.values()) {
            PackagePayItemEntity requestItem = requestMap.get(dbItem.getPayIndex());
            if (requestItem != null) {
                dbItem.setPaySdk1(requestItem.getPaySdk1());
                dbItem.setPaySdk2(requestItem.getPaySdk2());
                dbItem.setPaySdk3(requestItem.getPaySdk3());
                dbItem.setPaySdk1Price(requestItem.getPaySdk1Price());
                dbItem.setPaySdk2Price(requestItem.getPaySdk2Price());
                dbItem.setPaySdk3Price(requestItem.getPaySdk3Price());
                this.save(dbItem);
            } else {
                this.delete(dbItem.getId());
            }
        }
        for (PackagePayItemEntity requestItem : requestMap.values()) {
            if (!dbMap.containsKey(requestItem.getPayIndex())) {
                this.save(requestItem);
            }
        }
    }
    public Map<Integer, PackagePayItemEntity> toMap(List<PackagePayItemEntity> dbItems) {
        Map<Integer, PackagePayItemEntity> map = new HashMap<Integer, PackagePayItemEntity>();
        for (PackagePayItemEntity item : dbItems) {
            map.put(item.getPayIndex(), item);
        }
        return map;
    }
}
