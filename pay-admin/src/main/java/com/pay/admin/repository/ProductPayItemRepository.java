package com.pay.admin.repository;
import java.util.List;
import com.pay.admin.entity.ProductPayItemEntity;
import com.pay.admin.repository.framework.MyJpaRepository;

public interface ProductPayItemRepository extends MyJpaRepository<ProductPayItemEntity> {

    ProductPayItemEntity findByProductIdAndPayIndex(Long productId, int payIndex);

    List<ProductPayItemEntity> findByProductId(Long id);}
