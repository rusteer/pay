package com.pay.server.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.pay.server.entity.PackageEntity;

public interface PackageDao extends PagingAndSortingRepository<PackageEntity, Long>, JpaSpecificationExecutor<PackageEntity> {
    @Query("from PackageEntity a where a.buildStatus=?1  ")
    List<PackageEntity> getListByBuildStatus(String status);
    @Modifying
    @Query("update PackageEntity a set a.buildStatus=?2 where a.id=?1")
    void updateStatus(Long id, String status);
    @Modifying
    @Query("update PackageEntity a set a.buildStatus=?2,a.apkPath=?3 where a.id=?1")
    void updateStatusAndApkPath(Long id, String status, String apkPath);
}
