package com.pay.server.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.pay.server.entity.ChannelEntity;
import com.pay.server.repository.ChannelDao;

@Component
@Transactional
public class ChannelService extends AbstractService<ChannelEntity> {
    @Autowired
    private ChannelDao dao;
    @Override
    protected ChannelDao getDao() {
        return dao;
    }
    public ChannelEntity getByShortName(String shortName) {
        return dao.getByShortName(shortName);
    }
}
