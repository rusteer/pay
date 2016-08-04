package com.pay.server.service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.pay.server.entity.PackageEntity;
import com.pay.server.repository.PackageDao;

@Component
@Transactional
public class PackageService extends AbstractService<PackageEntity> {
    @Autowired
    private PackageDao dao;
    @Override
    protected PackageDao getDao() {
        return dao;
    }
    public List<PackageEntity> getPendingBuildList() {
        return dao.getListByBuildStatus("pending");
    }
    public void updateStatus(Long id, String status) {
        dao.updateStatus(id, status);
    }
    public void updateStatusAndApkPath(Long id, String status, String apkPath) {
        dao.updateStatusAndApkPath(id, status, apkPath);
    }
}
