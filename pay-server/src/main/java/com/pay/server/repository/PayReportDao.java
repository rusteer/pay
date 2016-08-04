package com.pay.server.repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.pay.server.entity.PayReportEntity;

public interface PayReportDao extends PagingAndSortingRepository<PayReportEntity, Long>, JpaSpecificationExecutor<PayReportEntity> {}
