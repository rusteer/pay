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
import com.pay.admin.bean.PayPointStat;
import com.pay.admin.controller.WebUtil;
import com.pay.admin.controller.admin.bean.PayPointCount;
import com.pay.admin.service.ChannelService;
import com.pay.admin.service.FormatUtil;
import com.pay.admin.service.PackageActivateStatService;
import com.pay.admin.service.PackagePaymentStatService;
import com.pay.admin.service.PackageService;
import com.pay.admin.service.PackageStatService;
import com.pay.admin.service.PayReportService;
import com.pay.admin.service.ProductService;
import com.pay.admin.service.SdkService;
import com.pay.admin.service.SettingService;

@Controller
@RequestMapping("/admin/" + PayPointController.cmpName)
public class PayPointController extends AbstractController {
    public static final String cmpName = "paypoint";
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
    @Autowired
    PayReportService payReportService;
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
        if (StringUtils.isNotBlank(to)) params.put("to", to);
        if (StringUtils.isBlank(from)) from = FormatUtil.format(new Date(System.currentTimeMillis() - 30L * 3600 * 24 * 1000));
        if (StringUtils.isNotBlank(from)) params.put("from", from);
        if (packageId + productId > 0) {
            Map<String, PayPointCount> map = new HashMap<String, PayPointCount>();
            PayPointCount allCount = new PayPointCount("总计", 0);
            for (PayPointStat stat : this.payReportService.getSumList(params)) {
                String key = stat.getPayName() + "-" + stat.getPrice();
                PayPointCount c = map.get(key);
                if (c == null) map.put(key, (c = new PayPointCount(stat.getPayName(), stat.getPrice())));
                switch (stat.getResult()) {
                    case 0:
                        c.setSuccessCount(c.getSuccessCount() + stat.getStatCount());
                        allCount.setSuccessCount(allCount.getSuccessCount() + stat.getStatCount());
                        break;
                    case 1:
                        c.setCancleCount(c.getCancleCount() + stat.getStatCount());
                        allCount.setCancleCount(allCount.getCancleCount() + stat.getStatCount());
                        break;
                    case 2:
                        c.setFailureCount(c.getFailureCount() + stat.getStatCount());
                        allCount.setFailureCount(allCount.getFailureCount() + stat.getStatCount());
                }
            }
            List<PayPointCount> statList = new ArrayList<PayPointCount>(map.values());
            Collections.sort(statList, new Comparator<PayPointCount>() {
                @Override
                public int compare(PayPointCount o1, PayPointCount o2) {
                    return Integer.valueOf(o2.getSuccessCount()).compareTo(o1.getSuccessCount());
                }
            });
            statList.add(allCount);
            for (PayPointCount c : statList) {
                c.setAllCount(c.getSuccessCount() + c.getCancleCount() + c.getFailureCount());
            }
            request.setAttribute("statList", statList);
        }
        request.setAttribute("channelList", this.channelService.getListOrderByHot());
        request.setAttribute("packageList", this.packageService.getList(productId, channelId));
        request.setAttribute("productList", this.productService.getListOrderByHot());
        request.setAttribute("sdkList", this.sdkService.getListOrderByHot());
        return render(request, "list");
    }
}
