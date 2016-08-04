package com.pay.server.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.pay.server.entity.DeviceEntity;

public interface DeviceDao extends PagingAndSortingRepository<DeviceEntity, Long>, JpaSpecificationExecutor<DeviceEntity> {
 
    //
    @Query("  from DeviceEntity a where a.imei=?1")
    List<DeviceEntity> getByImei(String imei);
    //
    @Query("  from DeviceEntity a where a.androidId=?1")
    List<DeviceEntity> getByAndroid(String androidId);
    //
    @Query("  from DeviceEntity a where a.macAddress=?1")
    List<DeviceEntity> getByMacAddress(String macAddress);
    //
    @Query("  from DeviceEntity a where a.serial=?1")
    List<DeviceEntity> getByMacSerial(String serial);
}
