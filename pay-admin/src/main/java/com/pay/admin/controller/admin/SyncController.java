package com.pay.admin.controller.admin;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.pay.admin.controller.WebUtil;
import com.pay.admin.controller.admin.bean.SdkPaymentSync;
import com.pay.admin.controller.admin.bean.Stat;
import com.pay.admin.entity.ChannelEntity;
import com.pay.admin.entity.PackageActivateStatEntity;
import com.pay.admin.entity.PackageEntity;
import com.pay.admin.entity.PackagePaymentStatEntity;
import com.pay.admin.entity.PackageStatEntity;
import com.pay.admin.entity.ProductEntity;
import com.pay.admin.entity.SdkEntity;
import com.pay.admin.service.ChannelService;
import com.pay.admin.service.FormatUtil;
import com.pay.admin.service.PackageActivateStatService;
import com.pay.admin.service.PackagePaymentStatService;
import com.pay.admin.service.PackageService;
import com.pay.admin.service.PackageStatService;
import com.pay.admin.service.ProductService;
import com.pay.admin.service.SdkService;
import com.pay.admin.service.SettingService;

@Controller
@RequestMapping("/admin/" + SyncController.cmpName)
public class SyncController extends AbstractController {
    public static final String cmpName = "sync";
    @Autowired
    private PackageService service;
    @Autowired
    private ProductService productService;
    @Autowired
    private PackageService packageService;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private SdkService sdkService;
    @Autowired
    private SettingService settingService;
    @Autowired
    PackageActivateStatService packageActivateStatService;
    @Autowired
    PackagePaymentStatService packagePaymentStatService;
    @Autowired
    PackageStatService packageStatService;
    @Override
    protected String getCmpName() {
        return cmpName;
    }
    @RequestMapping(value = "/{packageId}/{statDate}/", method = RequestMethod.GET)
    public String form(@PathVariable("packageId") Long packageId, @PathVariable("statDate") String statDate, HttpServletRequest request, HttpServletResponse response) {
        PackageStatEntity entity = this.packageStatService.getByUnique(statDate, packageId);
        if (entity == null) {
            entity = new PackageStatEntity();
            entity.setStatDate(statDate);
            entity.setPackageId(packageId);
        }
        loadData(request, entity);
        return render(request, "form");
    }
    private PackageStatEntity composeEntity(HttpServletRequest request) {
        String statDate = request.getParameter("statDate");
        long packageId = WebUtil.getLongParameter(request, "packageId");
        PackageStatEntity entity = this.packageStatService.getByUnique(statDate, packageId);
        if (entity == null) {
            entity = new PackageStatEntity();
            entity.setStatDate(statDate);
            entity.setPackageId(packageId);
        }
        PackageEntity pkg = this.packageService.get(packageId);
        if (pkg != null) {
            entity.setChannelId(pkg.getChannelId());
            entity.setProductId(pkg.getProductId());
        }
        entity.setDiscountActivateCount(WebUtil.getIntParameter(request, "discountActivateCount"));
        entity.setDiscountPayCount(WebUtil.getIntParameter(request, "discountPayCount"));
        entity.setDiscountPayEarning(WebUtil.getIntParameter(request, "discountPayEarning") * 100);
        return entity;
    }
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String save(HttpServletRequest request, HttpServletResponse response) {
        PackageStatEntity entity = composeEntity(request);
        boolean saveSuccess = false;
        try {
            logger.info("Saving entity:" + mapper.writeValueAsString(entity));
            entity = packageStatService.save(entity);
            for (Object o : request.getParameterMap().keySet()) {
                String name = (String) o;
                if (name.startsWith("sdk-")) {
                    long sdkId = Long.valueOf(name.substring(4));
                    int syncValue = WebUtil.getIntParameter(request, name);
                    this.packagePaymentStatService.updateSyncEarning(entity.getPackageId(), entity.getStatDate(), sdkId, syncValue * 100);
                }
            }
            saveSuccess = true;
        } catch (Throwable e) {
            logger.error("Failed to save entity", e);
            request.setAttribute("errorMessage", e.getMessage());
        }
        if (saveSuccess) return redirect(request, request.getParameter("redirectUrl"));
        loadData(request, entity);
        return render(request, "form");
    }
    public void loadData(HttpServletRequest request, PackageStatEntity entity) {
        request.setAttribute("entity", entity);
        List<PackagePaymentStatEntity> paymentList = this.packagePaymentStatService.findByPackageIdAndStatDate(entity.getPackageId(), entity.getStatDate());
        PackageEntity pkg = this.packageService.get(entity.getPackageId());
        Map<String, SdkEntity> sdkMap = this.sdkService.getMapByName();
        List<SdkPaymentSync> list = new ArrayList<SdkPaymentSync>();
        String paySdks = pkg.getPaySdks();
        if (StringUtils.isNotBlank(paySdks)) {
            for (String sdkName : paySdks.split(",")) {
                SdkEntity sdk = sdkMap.get(sdkName);
                if (sdk != null) {
                    int syncEarning = 0;
                    for (PackagePaymentStatEntity payment : paymentList) {
                        if (payment.getSdkId() == sdk.getId()) {
                            syncEarning = payment.getSyncEarning();
                            break;
                        }
                    }
                    SdkPaymentSync sync = new SdkPaymentSync();
                    sync.setSdk(sdk);
                    sync.setSyncEarning(syncEarning);
                    list.add(sync);
                }
            }
        }
        request.setAttribute("syncList", list);
    }
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String list(HttpServletRequest request, HttpServletResponse response) {
        long productId = WebUtil.getLongParameter(request, "productId");
        long channelId = WebUtil.getLongParameter(request, "channelId");
        String statDate = request.getParameter("statDate");
        boolean showAllPkgs = WebUtil.getBooleanParameter(request, "showAllPkgs");
        if (StringUtils.isBlank(statDate)) {
            statDate = FormatUtil.format(new Date());
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("statDate", statDate);
        if (productId > 0) params.put("productId", productId);
        if (channelId > 0) params.put("channelId", channelId);
        Map<Long, ProductEntity> productMap = this.productService.getMapAll();
        Map<Long, ChannelEntity> channelMap = this.channelService.getMapAll();
        List<PackageEntity> packageList = this.packageService.getList(productId, channelId);
        Map<Long, Stat> map = new HashMap<Long, Stat>();
        for (PackageEntity entity : packageList) {
            Stat stat = new Stat(entity.getId());
            stat.setPkg(entity);
            stat.setProduct(productMap.get(entity.getProductId()));
            stat.setChannel(channelMap.get(entity.getChannelId()));
            stat.setStatDate(statDate);
            map.put(entity.getId(), stat);
        }
        List<PackageActivateStatEntity> activateList = this.packageActivateStatService.getListByDate(params);
        List<PackagePaymentStatEntity> paymentList = this.packagePaymentStatService.getListByDate(params);
        for (PackageActivateStatEntity entity : activateList) {
            Stat stat = map.get(entity.getPackageId());
            if (stat != null) {
                stat.setActivateCount(entity.getActivateCount());
            }
        }
        for (PackagePaymentStatEntity entity : paymentList) {
            Stat stat = map.get(entity.getPackageId());
            if (stat != null) {
                stat.setPayCount(entity.getPayCount());
                stat.setPayEarning(entity.getPayEarning());
                stat.setSyncEarning(entity.getSyncEarning());
            }
        }
        for (PackageStatEntity entity : this.packageStatService.getListByDate(params)) {
            long packageId = entity.getPackageId();
            Stat stat = map.get(packageId);
            if (stat != null) {
                stat.setDiscountActivateCount(entity.getDiscountActivateCount());
                stat.setDiscountPayEarning(entity.getDiscountPayEarning());
            }
        }
        if (!showAllPkgs) {
            for (Long id : new ArrayList<Long>(map.keySet())) {
                Stat stat = map.get(id);
                if (stat.getActivateCount() + stat.getPayCount() + stat.getSyncEarning() + stat.getPayEarning() == 0) {
                    map.remove(id);
                }
            }
        }
        List<Stat> statList = new ArrayList<Stat>(map.values());
        Collections.sort(statList, new Comparator<Stat>() {
            @Override
            public int compare(Stat o1, Stat o2) {
                return Integer.valueOf(o2.getPayEarning()).compareTo(o1.getPayEarning());
            }
        });
        request.setAttribute("statDate", statDate);
        request.setAttribute("statList", statList);
        request.setAttribute("productList", this.productService.getListOrderByHot());
        request.setAttribute("channelList", this.channelService.getListOrderByHot());
        request.setAttribute("packageList", packageList);
        return render(request, "list");
    }
}
