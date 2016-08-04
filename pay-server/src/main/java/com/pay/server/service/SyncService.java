package com.pay.server.service;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.pay.server.entity.SyncEntity;
import com.pay.server.repository.SyncDao;

@Component
@Transactional
public class SyncService extends AbstractService<SyncEntity> {
    @Autowired
    private SyncDao dao;
    @Override
    protected SyncDao getDao() {
        return dao;
    }
    public boolean create(SyncEntity entity) {
        if (entity != null && (StringUtils.isBlank(entity.getLinkId()) || !hasSync(entity.getLinkId()))) {
            save(entity);
            return true;
        }
        return false;
    }
    private boolean hasSync(String linkId) {
        return dao.getByLinkId(linkId) != null;
    }
}
