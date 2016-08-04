package com.pay.admin.controller.admin;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.google.gson.Gson;
import com.pay.admin.controller.WebUtil;
import com.pay.admin.entity.ChannelEntity;
import com.pay.admin.entity.PackageEntity;
import com.pay.admin.entity.PackagePayItemEntity;
import com.pay.admin.entity.ProductEntity;
import com.pay.admin.entity.ProductPayItemEntity;
import com.pay.admin.entity.SdkEntity;
import com.pay.admin.service.BuildService;
import com.pay.admin.service.ChannelService;
import com.pay.admin.service.PackagePayItemService;
import com.pay.admin.service.PackageService;
import com.pay.admin.service.ProductPayItemService;
import com.pay.admin.service.ProductService;
import com.pay.admin.service.SdkService;
import com.pay.admin.service.SettingService;

@Controller
@RequestMapping("/admin/" + PackageController.cmpName)
public class PackageController extends AbstractController {
    private static Gson gson = new Gson();
    public static final String cmpName = "package";
    @Autowired
    private PackageService service;
    @Autowired
    private PackagePayItemService packagePayItemService;
    @Autowired
    private ProductPayItemService productPayItemService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private SdkService sdkService;
    @Autowired
    private SettingService settingService;
    private PackageEntity composeEntity(HttpServletRequest request) {
        PackageEntity entity = new PackageEntity();
        entity.setId(WebUtil.getLongParameter(request, "id"));
        entity.setProductId(WebUtil.getLongParameter(request, "productId"));
        entity.setChannelId(WebUtil.getLongParameter(request, "channelId"));
        entity.setPackageName(request.getParameter("packageName"));
        entity.setVersionName(request.getParameter("versionName"));
        entity.setVersionCode(WebUtil.getIntParameter(request, "versionCode"));
        entity.setAppName(request.getParameter("appName"));
        entity.setPaySdk(request.getParameter("paySdk"));
        if ("1".equals(request.getParameter("needBuild"))) {
            entity.setBuildStatus(BuildService.BUILD_STATUS_WAITING);
        } else if (entity.getId() > 0) {
            PackageEntity dbEntity = this.service.get(entity.getId());
            entity.setBuildStatus(dbEntity.getBuildStatus());
            entity.setApkPath(dbEntity.getApkPath());
        }
        entity.setName(request.getParameter("name"));
        entity.setLoginName(request.getParameter("loginName"));
        entity.setLoginPassword(request.getParameter("loginPassword"));
        entity.setShowPayCount("1".equals(request.getParameter("showPayCount")));
        entity.setShowPayEarning("1".equals(request.getParameter("showPayEarning")));
        entity.setShowActivateCount("1".equals(request.getParameter("showActivateCount")));
        entity.setPromotionType(request.getParameter("promotionType"));
        StringBuilder sb = new StringBuilder();
        for (String sdk : request.getParameterValues("paySdks")) {
            if (sb.length() > 0) sb.append(",");
            sb.append(sdk);
        }
        entity.setPaySdks(sb.toString());
        entity.setPayHintType(WebUtil.getIntParameter(request, "payHintType"));
        entity.setInfo(request.getParameter("info"));
        entity.setMarketPrice(WebUtil.getIntParameter(request, "marketPrice"));
        if (entity.getId() == 0) {
            entity.setCreateTime(new Date());
        }
        entity.setAutoSync("1".equals(request.getParameter("autoSync")));
        entity.setDiscountRate(WebUtil.getIntParameter(request, "discountRate"));
        entity.setDiscountStartCount(WebUtil.getIntParameter(request, "discountStartCount"));
        return entity;
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String form(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response) {
        PackageEntity entity = id > 0 ? service.get(id) : new PackageEntity(id);
        if (entity.getId() > 0) {
            ProductEntity product = this.productService.get(entity.getProductId());
            ChannelEntity channel = this.channelService.get(entity.getChannelId());
            StringBuilder buildFunc = new StringBuilder();
            buildFunc.append("String hostName=\"%s\";\n");
            buildFunc.append("String packageObj=\"%s\";\n");
            buildFunc.append("String channelObj=\"%s\";\n");
            buildFunc.append("String productObj=\"%s\";\n");
            buildFunc.append("doMerger(hostName,packageObj,channelObj,productObj);\n");
            String attrValue = String.format(buildFunc.toString(), settingService.get(1L).getHostName(), base64Encode(entity), base64Encode(channel), base64Encode(product));
            request.setAttribute("buildFunc", attrValue);
        }
        loadData(request, entity);
        return render(request, "form");
    }
    @Override
    protected String getCmpName() {
        return cmpName;
    }
    private static String base64Encode(Object src) {
        try {
            return new String(Base64.encodeBase64(gson.toJson(src).getBytes("UTF-8")), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String list(HttpServletRequest request, HttpServletResponse response) {
        List<PackageEntity> list = new ArrayList<PackageEntity>();
        long productId = WebUtil.getLongParameter(request, "productId");
        long channelId = WebUtil.getLongParameter(request, "channelId");
        String sdk=request.getParameter("sdk");
        Map<Long, ProductEntity> productMap = this.productService.getMapAll();
        Map<Long, ChannelEntity> channelMap = this.channelService.getMapAll();
        request.setAttribute("productMap", productMap);
        request.setAttribute("channelMap", channelMap);
        request.setAttribute("productList", this.productService.getListOrderByHot());
        request.setAttribute("channelList", this.channelService.getListOrderByHot());
        request.setAttribute("sdkList", this.sdkService.getListOrderByHot());
        request.setAttribute("sdkMap", this.sdkService.getMapByName());
        if (productId > 0 || channelId > 0 || StringUtils.isNotBlank(sdk)) {
            for (PackageEntity entity : service.getAll()) {
                boolean matchProduct = productId == 0 || entity.getProductId() == productId;
                boolean matchChannel = channelId == 0 || entity.getChannelId() == channelId;
                boolean matchSdk=StringUtils.isBlank(sdk) || (entity.getPaySdk().contains(sdk)|| entity.getPaySdks().contains(sdk));
                if (matchProduct && matchChannel&& matchSdk) {
                    list.add(entity);
                }
            }
            Map<String, SdkEntity> sdkMap = sdkService.getMapByName();
            for (PackageEntity entity : list) {
                String paySdks = entity.getPaySdks();
                if (StringUtils.isNotBlank(paySdks)) {
                    StringBuilder sb = new StringBuilder();
                    for (String paySdk : paySdks.split(",")) {
                        if (sdkMap.containsKey(paySdk)) {
                            if (sb.length() > 0) sb.append(",");
                            sb.append(sdkMap.get(paySdk).getDisplayName());
                        }
                    }
                    entity.setPaySdks(sb.toString());
                }
                if (sdkMap.containsKey(entity.getPaySdk())) {
                    entity.setPaySdk(sdkMap.get(entity.getPaySdk()).getDisplayName());
                }
            }
            request.setAttribute("list", list);
        }
        return render(request, "list");
    }
    public void loadData(HttpServletRequest request, PackageEntity entity) {
        request.setAttribute("entity", entity);
        request.setAttribute("productList", this.productService.getListOrderByHot());
        request.setAttribute("channelList", this.channelService.getListOrderByHot());
        request.setAttribute("sdkList", this.sdkService.getListOrderByHot());
        request.setAttribute("productMap", this.productService.getMapAll());
        request.setAttribute("channelMap", this.channelService.getMapAll());
        request.setAttribute("sdkMap", this.sdkService.getMapByName());
        if (entity.getId() > 0 && entity.getProductId() > 0) {
            List<ProductPayItemEntity> productItems = productPayItemService.findByProductId(entity.getProductId());
            Map<Integer, PackagePayItemEntity> map = new HashMap<Integer, PackagePayItemEntity>();
            List<PackagePayItemEntity> packageItemList = new ArrayList<PackagePayItemEntity>();
            for (PackagePayItemEntity item : packagePayItemService.findByPackageId(entity.getId())) {
                map.put(item.getPayIndex(), item);
            }
            for (ProductPayItemEntity productItem : productItems) {
                int payIndex = productItem.getPayIndex();
                PackagePayItemEntity packageItem = map.get(payIndex);
                if (packageItem == null) {
                    packageItem = new PackagePayItemEntity();
                    packageItem.setPayIndex(payIndex);
                    packageItem.setPackageId(entity.getId());
                }
                packageItem.setPayName(productItem.getPayName());
                packageItem.setPayPrice(productItem.getPayPrice());
                packageItemList.add(packageItem);
            }
            request.setAttribute("packageItemList", packageItemList);
        }
    }
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String save(HttpServletRequest request, HttpServletResponse response) {
        PackageEntity entity = composeEntity(request);
        boolean saveSuccess = false;
        try {
            logger.info("Saving entity:" + mapper.writeValueAsString(entity));
            entity = service.save(entity);
            savePayItems(request, entity);
            saveSuccess = true;
        } catch (Throwable e) {
            logger.error("Failed to save entity", e);
            request.setAttribute("errorMessage", e.getMessage());
        }
        if (saveSuccess) return redirect(request, "/admin/" + cmpName + "/" + entity.getId() + "?saveSuccess=true");
        request.setAttribute("saveSuccess", saveSuccess);
        loadData(request, entity);
        return render(request, "form");
    }
    private void savePayItems(HttpServletRequest request, PackageEntity entity) {
        ProductEntity product = this.productService.get(entity.getProductId());
        List<ProductPayItemEntity> productPayItems = this.productPayItemService.findByProductId(product.getId());
        List<PackagePayItemEntity> packageItems = new ArrayList<PackagePayItemEntity>();
        for (ProductPayItemEntity item : productPayItems) {
            int payIndex = item.getPayIndex();
            long sdk1 = WebUtil.getLongParameter(request, "payIndex-" + payIndex + "-sdk1");
            int price1 = sdk1 > 0 ? WebUtil.getIntParameter(request, "payIndex-" + payIndex + "-price1") : 0;
            long sdk2 = WebUtil.getLongParameter(request, "payIndex-" + payIndex + "-sdk2");
            int price2 = sdk2 > 0 ? WebUtil.getIntParameter(request, "payIndex-" + payIndex + "-price2") : 0;
            long sdk3 = WebUtil.getLongParameter(request, "payIndex-" + payIndex + "-sdk3");
            int price3 = sdk3 > 0 ? WebUtil.getIntParameter(request, "payIndex-" + payIndex + "-price3") : 0;
            if (sdk1 + sdk2 + sdk3 > 0) {
                PackagePayItemEntity pItem = new PackagePayItemEntity();
                pItem.setPackageId(entity.getId());
                pItem.setPayIndex(payIndex);
                pItem.setPaySdk1(sdk1);
                pItem.setPaySdk1Price(price1);
                pItem.setPaySdk2(sdk2);
                pItem.setPaySdk2Price(price2);
                pItem.setPaySdk3(sdk3);
                pItem.setPaySdk3Price(price3);
                packageItems.add(pItem);
            }
        }
        this.packagePayItemService.saveOrUpdate(entity.getId(), packageItems);
    }
}
