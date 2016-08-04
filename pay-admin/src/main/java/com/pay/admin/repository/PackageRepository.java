package com.pay.admin.repository;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import com.pay.admin.entity.PackageEntity;
import com.pay.admin.repository.framework.MyJpaRepository;

public interface PackageRepository extends MyJpaRepository<PackageEntity> {
    @Query("from PackageEntity a where a.buildStatus=?1  ")
    List<PackageEntity> getListByBuildStatus(int status);
    @Modifying
    @Query("update PackageEntity a set a.buildStatus=?2 where a.id=?1")
    void updateStatus(Long id, int status);
    @Modifying
    @Query("update PackageEntity a set a.buildStatus=?2,a.apkPath=?3 where a.id=?1")
    void updateStatusAndApkPath(Long id, int status, String apkPath);
    List<PackageEntity> findByProductIdAndChannelId(long productId, long channelId);
    List<PackageEntity> findByProductId(long productId);
    List<PackageEntity> findByChannelId(long channelId);
    PackageEntity findByLoginName(String name);
}
