package com.pay.admin.service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.pay.admin.entity.ProductEntity;
import com.pay.admin.repository.ProductRepository;
import com.pay.admin.service.framework.AbstractService;

@Component
@Transactional(readOnly = true)
public class ProductService extends AbstractService<ProductEntity> {
    @Autowired
    private ProductRepository dao;
    @Override
    protected ProductRepository getRepository() {
        return dao;
    }
    
    public List<ProductEntity> getListOrderByHot(){
        return dao.getListOrderByHot();
    }
}
