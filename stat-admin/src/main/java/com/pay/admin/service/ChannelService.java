package com.pay.admin.service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.pay.admin.entity.ChannelEntity;
import com.pay.admin.repository.ChannelRepository;
import com.pay.admin.service.framework.AbstractService;

@Component
@Transactional(readOnly = true)
public class ChannelService extends AbstractService<ChannelEntity> {
    @Autowired
    private ChannelRepository dao;
    @Override
    protected ChannelRepository getRepository() {
        return dao;
    }
    public List<ChannelEntity> getListOrderByHot() {
        return dao.getListOrderByHot();
    }
    public ChannelEntity findByLoginName(String name) {
        return dao.findByLoginName(name);
    }
}
