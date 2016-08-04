package com.pay.server.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.pay.server.entity.ProductEntity;
import com.pay.server.repository.ProductDao;

@Component
@Transactional
public class ProductService extends AbstractService<ProductEntity>{
    @Autowired
    private ProductDao dao;

    @Override
    protected ProductDao getDao() {
        return dao;
    }
}
