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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.pay.admin.controller.WebUtil;
import com.pay.admin.entity.PackageActivateStatEntity;
import com.pay.admin.entity.PackagePaymentStatEntity;
import com.pay.admin.entity.PackageStatEntity;
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
    public class Stat {
        private String statDate;
        private int activateCount;
        private int payCount;
        private int payEarning;
        private int syncEarning;
        private int discountActivateCount; //给渠道看的数据
        private int discountPayEarning;//扣量的付费金额
        public Stat(String statDate) {
            this.statDate = statDate;
        }
        public String getStatDate() {
            return statDate;
        }
        public int getActivateCount() {
            return activateCount;
        }
        public int getPayCount() {
            return payCount;
        }
        public int getPayEarning() {
            return payEarning;
        }
        public int getSyncEarning() {
            return syncEarning;
        }
        public int getDiscountActivateCount() {
            return discountActivateCount;
        }
        public int getDiscountPayEarning() {
            return discountPayEarning;
        }
        public void setStatDate(String statDate) {
            this.statDate = statDate;
        }
        public void setActivateCount(int activateCount) {
            this.activateCount = activateCount;
        }
        public void setPayCount(int payCount) {
            this.payCount = payCount;
        }
        public void setPayEarning(int payEarning) {
            this.payEarning = payEarning;
        }
        public void setSyncEarning(int syncEarning) {
            this.syncEarning = syncEarning;
        }
        public void setDiscountActivateCount(int discountActivateCount) {
            this.discountActivateCount = discountActivateCount;
        }
        public void setDiscountPayEarning(int discountPayEarning) {
            this.discountPayEarning = discountPayEarning;
        }
    }
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String list(HttpServletRequest request, HttpServletResponse response) {
        long packageId = WebUtil.getLongParameter(request, "packageId");
        long productId = WebUtil.getLongParameter(request, "productId");
        long channelId = WebUtil.getLongParameter(request, "channelId");
        long sdkId = WebUtil.getLongParameter(request, "sdkId");
        String from = request.getParameter("from");
        String to = request.getParameter("to");
        Map<String, Object> params = new HashMap<String, Object>();
        if (packageId > 0) {
            params.put("packageId", packageId);
        } else {
            if (productId > 0) params.put("productId", productId);
            if (channelId > 0) params.put("channelId", channelId);
        }
        if (sdkId > 0) params.put("sdkId", sdkId);
        if (StringUtils.isBlank(from)) from = FormatUtil.format(new Date(System.currentTimeMillis() - 30L * 3600 * 24 * 1000));
        params.put("from", from);
        if (StringUtils.isNotBlank(to)) params.put("to", to);
        Map<String, Stat> map = new HashMap<String, Stat>();
        for (PackageActivateStatEntity entity : packageActivateStatService.getSumList(params)) {
            String statDate = entity.getStatDate();
            Stat stat = map.get(statDate);
            if (stat == null) map.put(statDate, (stat = new Stat(statDate)));
            stat.setActivateCount(entity.getActivateCount());
        }
        for (PackagePaymentStatEntity entity : this.packagePaymentStatService.getSumList(params)) {
            String statDate = entity.getStatDate();
            Stat stat = map.get(statDate);
            if (stat == null) map.put(statDate, (stat = new Stat(statDate)));
            stat.setPayCount(entity.getPayCount());
            stat.setPayEarning(entity.getPayEarning());
            stat.setSyncEarning(entity.getSyncEarning());
        }
        for (PackageStatEntity entity : this.packageStatService.getSumList(params)) {
            String statDate = entity.getStatDate();
            Stat stat = map.get(statDate);
            if (stat == null) map.put(statDate, (stat = new Stat(statDate)));
            stat.setDiscountActivateCount(entity.getDiscountActivateCount());
            stat.setDiscountPayEarning(entity.getDiscountPayEarning());
        }
        List<Stat> statList = new ArrayList<Stat>(map.values());
        Collections.sort(statList, new Comparator<Stat>() {
            @Override
            public int compare(Stat o1, Stat o2) {
                return o2.getStatDate().compareTo(o1.getStatDate());
            }
        });
        request.setAttribute("statList", statList);
        request.setAttribute("packageList", this.packageService.getList(productId, channelId));
        request.setAttribute("productList", this.productService.getListOrderByHot());
        request.setAttribute("channelList", this.channelService.getListOrderByHot());
        request.setAttribute("sdkList", this.sdkService.getListOrderByHot());
        request.setAttribute("from", from);
        return render(request, "list");
    }
}
