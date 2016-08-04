package com.pay.admin.service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.pay.admin.entity.PackageEntity;
import com.pay.admin.repository.PackageRepository;
import com.pay.admin.service.framework.AbstractService;

@Component
@Transactional(readOnly = true)
public class PackageService extends AbstractService<PackageEntity> {
    @Autowired
    private PackageRepository dao;
    @Override
    protected PackageRepository getRepository() {
        return dao;
    }
    public List<PackageEntity> getList(long productId, long channelId) {
        if (productId > 0 && channelId > 0) {
            return dao.findByProductIdAndChannelId(productId, channelId);
        } else if (productId > 0) {
            return dao.findByProductId(productId);
        } else if (channelId > 0) {
            return dao.findByChannelId(channelId);
        } else {
            return this.getAll();
        }
    }
    public Object getList(long channelId) {
        return dao.findByChannelId(channelId);
    }
    public PackageEntity findByLoginName(String name) {
        return dao.findByLoginName(name);
    }
}
