package com.pay.server.repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.pay.server.entity.ProductEntity;

public interface ProductDao extends PagingAndSortingRepository<ProductEntity, Long>, JpaSpecificationExecutor<ProductEntity> {
     
}
