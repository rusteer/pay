package com.pay.admin.controller.channel;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pay.admin.controller.WebUtil;
import com.pay.admin.entity.ChannelEntity;
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

@Controller(value = "channelControllerClient")
@RequestMapping("/channel")
public class ChannelController {
    private static final String CHANNEL_ID = "channelId";
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
    protected Logger logger = LoggerFactory.getLogger(getClass());
    protected ObjectMapper mapper = new ObjectMapper();
    protected String redirect(HttpServletRequest request, String url) {
        request.setAttribute("url", url);
        return "redirect";
    }
    protected String render(HttpServletRequest request, String name) {
        return "channel/" + name;
    }
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(HttpServletRequest request, HttpServletResponse response) {
        return render(request, "login");
    }
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().removeAttribute(CHANNEL_ID);
        return render(request, "login");
    }
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String doLogin(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        if (StringUtils.isNotBlank(name) && StringUtils.isNotBlank(password)) {
            ChannelEntity channel = this.channelService.findByLoginName(name);
            if (channel != null && channel.getPassword().equals(password)) {
                request.getSession().setAttribute(CHANNEL_ID, channel.getId());
                return redirect(request, "/channel/report/");
            }
        }
        request.setAttribute("error", "true");
        return render(request, "login");
    }
    @RequestMapping(value = "/report/", method = RequestMethod.GET)
    public String report(HttpServletRequest request, HttpServletResponse response) {
        Long channelId = (Long) request.getSession().getAttribute(CHANNEL_ID);
        if (channelId == null) { return this.redirect(request, "/channel/login"); }
        ChannelEntity channel = this.channelService.get(channelId);
        request.setAttribute("channel", channel);
        long packageId = WebUtil.getLongParameter(request, "packageId");
        String from = request.getParameter("from");
        String to = request.getParameter("to");
        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtils.isBlank(from)) {
            from=FormatUtil.format(new Date(System.currentTimeMillis() - 20 * 3600 * 24 * 1000));
        }
        params.put(CHANNEL_ID, channelId);
        params.put("from", from);
        if (StringUtils.isNotBlank(to)) params.put("to", to);
        if (packageId > 0) params.put("packageId", packageId);
        request.setAttribute("packageList", this.packageService.getList(channelId));
        List<PackageStatEntity> statList = this.packageStatService.getChannelSumList(params);
        request.setAttribute("statList", statList);
        request.setAttribute("cmpName", packageId);
        request.setAttribute("from", from);
        return render(request, "report");
    }
}
