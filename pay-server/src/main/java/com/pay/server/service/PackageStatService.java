package com.pay.server.service;
import java.util.Date;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.pay.server.entity.ChannelEntity;
import com.pay.server.entity.PackageEntity;
import com.pay.server.entity.PackageStatEntity;
import com.pay.server.repository.PackageStatDao;
import com.pay.server.utils.FormatUtil;

@Component
@Transactional
public class PackageStatService extends AbstractService<PackageStatEntity> {
    @Autowired
    private PackageStatDao dao;
    @Autowired
    private ChannelService channelService;
    @Autowired
    PackageActivateStatService activateService;
    private static Random random = new Random();
    @Override
    protected PackageStatDao getDao() {
        return dao;
    }
    private static boolean shouldDiscount(int discountRate) {
        if (discountRate < 0) discountRate = 0;
        if (discountRate > 100) discountRate = 100;
        int randomNum = random.nextInt(100);
        return discountRate > randomNum;
    }
    public static void main(String args[]) {
        int count = 0;
        int discountRate = 20;
        for (int i = 0; i < 100000; i++) {
            if (!shouldDiscount(discountRate)) {
                count++;
            }
        }
        System.out.println(count);
    }
    private PackageStatEntity touch(PackageEntity pkg, String statDate) {
        PackageStatEntity entity = dao.getByUniqeKeys(pkg.getId(), statDate);
        if (entity == null) {
            entity = new PackageStatEntity();
            entity.setPackageId(pkg.getId());
            entity.setChannelId(pkg.getChannelId());
            entity.setProductId(pkg.getProductId());
            entity.setStatDate(statDate);
            entity = dao.save(entity);
        }
        return entity;
    }
    public void increaseActivateCount(PackageEntity pkg) {
        boolean autoSync = pkg.isAutoSync();
        int discountRate = pkg.getDiscountRate();
        int discountStartCount = pkg.getDiscountStartCount();
        if (!autoSync) {
            ChannelEntity channel = channelService.get(pkg.getChannelId());
            autoSync = channel.isAutoSync();
            discountRate = channel.getDiscountRate();
            discountStartCount = channel.getDiscountStartCount();
        }
        if (autoSync) {
            String statDate = FormatUtil.format(new Date());
            touch(pkg, statDate);
            int existingActivateCount = activateService.touch(pkg, statDate).getActivateCount();
            if (existingActivateCount < discountStartCount || !shouldDiscount(discountRate)) {
                dao.increaseDiscountActivateCount(pkg.getId(), statDate);
            }
        }
    }
   
}
