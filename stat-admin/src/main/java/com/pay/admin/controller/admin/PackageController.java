package com.pay.admin.controller.admin;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.pay.admin.controller.WebUtil;
import com.pay.admin.entity.ChannelEntity;
import com.pay.admin.entity.PackageEntity;
import com.pay.admin.entity.ProductEntity;
import com.pay.admin.service.ChannelService;
import com.pay.admin.service.PackageService;
import com.pay.admin.service.ProductService;

@Controller
@RequestMapping("/admin/" + PackageController.cmpName)
public class PackageController extends AbstractController {
    public static final String cmpName = "package";
    @Autowired
    private PackageService service;
    @Autowired
    private ProductService productService;
    @Autowired
    private ChannelService channelService;
    private PackageEntity composeEntity(HttpServletRequest request) {
        PackageEntity entity = new PackageEntity();
        entity.setId(WebUtil.getLongParameter(request, "id"));
        entity.setProductId(WebUtil.getLongParameter(request, "productId"));
        entity.setChannelId(WebUtil.getLongParameter(request, "channelId"));
        entity.setPackageName(request.getParameter("packageName"));
        entity.setVersionName(request.getParameter("versionName"));
        entity.setVersionCode(WebUtil.getIntParameter(request, "versionCode"));
        entity.setAppName(request.getParameter("appName"));
        entity.setName(request.getParameter("name"));
        entity.setLoginName(request.getParameter("loginName"));
        entity.setLoginPassword(request.getParameter("loginPassword"));
        entity.setShowPayCount("1".equals(request.getParameter("showPayCount")));
        entity.setShowPayEarning("1".equals(request.getParameter("showPayEarning")));
        entity.setShowActivateCount("1".equals(request.getParameter("showActivateCount")));
        entity.setPromotionType(request.getParameter("promotionType"));
        if (entity.getId() == 0) {
            entity.setCreateTime(new Date());
        }
        return entity;
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String form(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response) {
        PackageEntity entity = id > 0 ? service.get(id) : new PackageEntity(id);
        loadData(request, entity);
        return render(request, "form");
    }
    @Override
    protected String getCmpName() {
        return cmpName;
    }
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String list(HttpServletRequest request, HttpServletResponse response) {
        List<PackageEntity> list = new ArrayList<PackageEntity>();
        long productId = WebUtil.getLongParameter(request, "productId");
        long channelId = WebUtil.getLongParameter(request, "channelId");
        Map<Long, ProductEntity> productMap = this.productService.getMapAll();
        Map<Long, ChannelEntity> channelMap = this.channelService.getMapAll();
        request.setAttribute("productMap", productMap);
        request.setAttribute("channelMap", channelMap);
        request.setAttribute("productList", this.productService.getListOrderByHot());
        request.setAttribute("channelList", this.channelService.getListOrderByHot());
        if (productId > 0 || channelId > 0) {
            for (PackageEntity entity : service.getAll()) {
                if ((productId == 0 || entity.getProductId() == productId) && (channelId == 0 || entity.getChannelId() == channelId)) {
                    list.add(entity);
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
        request.setAttribute("productMap", this.productService.getMapAll());
        request.setAttribute("channelMap", this.channelService.getMapAll());
    }
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String save(HttpServletRequest request, HttpServletResponse response) {
        PackageEntity entity = composeEntity(request);
        boolean saveSuccess = false;
        try {
            logger.info("Saving entity:" + mapper.writeValueAsString(entity));
            entity = service.save(entity);
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
}
