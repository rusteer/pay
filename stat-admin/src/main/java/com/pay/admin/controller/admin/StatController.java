package com.pay.admin.controller.admin;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.pay.admin.controller.WebUtil;
import com.pay.admin.entity.PackageStatEntity;
import com.pay.admin.service.ChannelService;
import com.pay.admin.service.PackageService;
import com.pay.admin.service.PackageStatService;
import com.pay.admin.service.ProductService;

@Controller
@RequestMapping("/admin/" + StatController.cmpName)
public class StatController extends AbstractController {
    public static final String cmpName = "stat";
    @Autowired
    private PackageService service;
    @Autowired
    private ProductService productService;
    @Autowired
    private PackageService packageService;
    @Autowired
    private ChannelService channelService;
    @Autowired
    PackageStatService packageStatService;
    @Override
    protected String getCmpName() {
        return cmpName;
    }
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private Date getDate(String date) {
        try {
            return format.parse(date);
        } catch (Exception e) {}
        return null;
    }
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String list(HttpServletRequest request, HttpServletResponse response) {
        long packageId = WebUtil.getLongParameter(request, "packageId");
        long productId = WebUtil.getLongParameter(request, "productId");
        long channelId = WebUtil.getLongParameter(request, "channelId");
        Map<String, Object> params = new HashMap<String, Object>();
        Date fromDate = getDate(request.getParameter("from"));
        if (fromDate == null) {
            fromDate = new Date(System.currentTimeMillis() - 30L * 3600 * 24 * 1000);
        }
        Date toDate = getDate(request.getParameter("to"));
        if (toDate == null) {
            toDate = new Date(System.currentTimeMillis());
        }
        String from = format.format(fromDate);
        String to = format.format(toDate);
        params.put("from", from);
        params.put("to", to);
        if (packageId > 0) {
            params.put("packageId", packageId);
        } else {
            if (productId > 0) params.put("productId", productId);
            if (channelId > 0) params.put("channelId", channelId);
        }
        Map<String, PackageStatEntity> map = new HashMap<String, PackageStatEntity>();
        while (format.format(fromDate).compareTo(format.format(toDate)) < 1) {
            String statDate = format.format(fromDate);
            map.put(statDate, new PackageStatEntity(statDate));
            fromDate = new Date(fromDate.getTime() + 3600 * 24 * 1000);
        }
        for (PackageStatEntity entity : this.packageStatService.getSumList(params)) {
            String statDate = entity.getStatDate();
            map.put(statDate, entity);
        }
        List<PackageStatEntity> list = new ArrayList<PackageStatEntity>(map.values());
        Collections.sort(list, new Comparator<PackageStatEntity>() {
            @Override
            public int compare(PackageStatEntity o1, PackageStatEntity o2) {
                return o2.getStatDate().compareTo(o1.getStatDate());
            }
        });
        if (packageId > 0) for (PackageStatEntity stat : list) {
            stat.setPackageId(packageId);
        }
        request.setAttribute("list", list);
        request.setAttribute("packageList", this.packageService.getList(productId, channelId));
        request.setAttribute("productList", this.productService.getListOrderByHot());
        request.setAttribute("channelList", this.channelService.getListOrderByHot());
        request.setAttribute("from", from);
        request.setAttribute("to", to);
        return render(request, "list");
    }
    private PackageStatEntity composeEntity(HttpServletRequest request) {
        PackageStatEntity entity = new PackageStatEntity();
        entity.setPackageId(WebUtil.getLongParameter(request, "packageId"));
        entity.setStatDate(request.getParameter("statDate"));
        entity.setActivateCount(WebUtil.getIntParameter(request, "activateCount"));
        entity.setDiscountActivateCount(WebUtil.getIntParameter(request, "discountActivateCount"));
        entity.setDiscountPayCount(WebUtil.getIntParameter(request, "discountPayCount"));
        entity.setDiscountPayEarning(WebUtil.getIntParameter(request, "discountPayEarning"));
        entity.setPayCount(WebUtil.getIntParameter(request, "payCount"));
        entity.setPayEarning(WebUtil.getIntParameter(request, "payEarning"));
        entity.setSyncEarning(WebUtil.getIntParameter(request, "syncEarning"));
        return entity;
    }
    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String form(HttpServletRequest request, HttpServletResponse response) {
        String statDate = request.getParameter("statDate");
        long packageId = WebUtil.getLongParameter(request, "packageId");
        PackageStatEntity entity = this.packageStatService.load(packageId, statDate);
        if (entity == null) {
            entity = new PackageStatEntity();
            entity.setPackageId(packageId);
            entity.setStatDate(statDate);
        }
        request.setAttribute("entity", entity);
        return render(request, "form");
    }
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String save(HttpServletRequest request, HttpServletResponse response) {
        PackageStatEntity entity = composeEntity(request);
        boolean saveSuccess = false;
        try {
            logger.info("Saving entity:" + mapper.writeValueAsString(entity));
            packageStatService.loadAndSave(entity);
            saveSuccess = true;
        } catch (Throwable e) {
            logger.error("Failed to save entity", e);
            request.setAttribute("errorMessage", e.getMessage());
        }
        if (saveSuccess) return redirect(request, request.getParameter("redirect"));
        request.setAttribute("saveSuccess", saveSuccess);
        request.setAttribute("entity", entity);
        return render(request, "form");
    }
}
