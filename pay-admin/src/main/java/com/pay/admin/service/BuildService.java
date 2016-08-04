package com.pay.admin.service;
import java.util.List;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.google.gson.Gson;
import com.pay.admin.build.build.Merger;
import com.pay.admin.entity.ChannelEntity;
import com.pay.admin.entity.PackageEntity;
import com.pay.admin.entity.ProductEntity;
import com.pay.admin.entity.SettingEntity;

@Component
@Transactional
public class BuildService {
    public static final int BUILD_STATUS_WAITING = 1;
    public static final int BUILD_STATUS_BUILDING = 2;
    public static final int BUILD_STATUS_SUCCESS = 3;
    public static final int BUILD_STATUS_FAILURE = 4;
    protected Logger logger = LoggerFactory.getLogger("build");
    private Gson gson = new Gson();
    @Autowired
    private ProductService productService;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private PackageService pkgService;
    @Autowired
    private SettingService settingService;
    private static final Object locker = new Object();
    private static boolean building = false;
    public void build() {
        synchronized (locker) {
            if (building) return;
        }
        building = true;
        try {
            doBuild();
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        }
        building = false;
    }
    private void doBuild() throws Throwable {
        List<PackageEntity> buildList = pkgService.getPendingBuildList();
        if (buildList.size() > 0) {
            PackageEntity pkg = buildList.get(0);
            pkgService.updateStatus(pkg.getId(),BUILD_STATUS_BUILDING);
            try {
                ChannelEntity channel = this.channelService.get(pkg.getChannelId());
                ProductEntity product = this.productService.get(pkg.getProductId());
                String[] sdks = pkg.getPaySdks().split(",");
                SettingEntity setting = this.settingService.get(1L);
                String hostName = setting.getHostName();
                JSONObject channelJson = new JSONObject(gson.toJson(channel));
                JSONObject pkgJson = new JSONObject(gson.toJson(pkg));
                JSONObject productJson = new JSONObject(gson.toJson(product));
                String apkPath = Merger.doMerge(hostName, pkgJson, channelJson, productJson, sdks);
                pkgService.updateStatusAndApkPath(pkg.getId(), BUILD_STATUS_SUCCESS, "http://" + hostName + apkPath);
            } catch (Throwable e) {
                pkgService.updateStatus(pkg.getId(),BUILD_STATUS_FAILURE);
                logger.error(e.getMessage(), e);
            }
        }
    }
}
