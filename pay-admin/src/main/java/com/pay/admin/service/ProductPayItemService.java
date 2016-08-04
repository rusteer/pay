package com.pay.admin.service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.pay.admin.entity.ProductPayItemEntity;
import com.pay.admin.repository.ProductPayItemRepository;
import com.pay.admin.service.framework.AbstractService;

@Component
@Transactional(readOnly = true)
public class ProductPayItemService extends AbstractService<ProductPayItemEntity> {
    @Autowired
    private ProductPayItemRepository dao;
    @Override
    protected ProductPayItemRepository getRepository() {
        return dao;
    }
    @Transactional(readOnly = false)
    public void saveOrUpdate(ProductPayItemEntity item) {
        ProductPayItemEntity databaseItem = this.dao.findByProductIdAndPayIndex(item.getProductId(), item.getPayIndex());
        if (databaseItem == null) {
            databaseItem = item;
        } else {
            databaseItem.setPayName(item.getPayName());
            databaseItem.setPayPrice(item.getPayPrice());
        }
        this.save(databaseItem);
    }
    public List<ProductPayItemEntity> findByProductId(Long id) {
        return dao.findByProductId(id);
    }
    @Transactional(readOnly = false)
    public void saveOrUpdate(Long id, Map<Integer, ProductPayItemEntity> map) {
        Map<Integer, ProductPayItemEntity> dbMap = new HashMap<Integer, ProductPayItemEntity>();
        List<ProductPayItemEntity> dbList = this.findByProductId(id);
        for (ProductPayItemEntity item : dbList) {
            dbMap.put(item.getPayIndex(), item);
        }
        for (ProductPayItemEntity dbItem : dbList) {
            if (map.containsKey(dbItem.getPayIndex())) {
                dbItem.setPayName(map.get(dbItem.getPayIndex()).getPayName());
                dbItem.setPayPrice(map.get(dbItem.getPayIndex()).getPayPrice());
                this.save(dbItem);
            } else {
                this.delete(dbItem.getId());
            }
        }
        for (ProductPayItemEntity item : map.values()) {
            if (!dbMap.containsKey(item.getPayIndex())) {
                this.save(item);
            }
        }
    }
}
