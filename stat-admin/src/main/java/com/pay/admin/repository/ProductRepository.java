package com.pay.admin.repository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import com.pay.admin.entity.ProductEntity;
import com.pay.admin.repository.framework.MyJpaRepository;

public interface ProductRepository extends MyJpaRepository<ProductEntity> {
    @Query("from ProductEntity a order by a.hot desc  ")
    List<ProductEntity> getListOrderByHot();
}
