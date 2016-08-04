package com.pay.admin.controller.pkg;
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
import com.pay.admin.entity.PackageEntity;
import com.pay.admin.entity.PackageStatEntity;
import com.pay.admin.service.FormatUtil;
import com.pay.admin.service.PackageService;
import com.pay.admin.service.PackageStatService;
import com.pay.admin.service.SettingService;

@Controller(value = "packageControllerClient")
@RequestMapping("/package")
public class PackageController {
    private static final String PACKAGE_ID = "packageId";
    @Autowired
    private PackageService service;
    @Autowired
    private PackageService packageService;
    @Autowired
    private SettingService settingService;
    @Autowired
    PackageStatService packageStatService;
    protected Logger logger = LoggerFactory.getLogger(getClass());
    protected ObjectMapper mapper = new ObjectMapper();
    protected String redirect(HttpServletRequest request, String url) {
        request.setAttribute("url", url);
        return "redirect";
    }
    protected String render(HttpServletRequest request, String name) {
        return "package/" + name;
    }
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(HttpServletRequest request, HttpServletResponse response) {
        return render(request, "login");
    }
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().removeAttribute(PACKAGE_ID);
        return render(request, "login");
    }
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String doLogin(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        if (StringUtils.isNotBlank(name) && StringUtils.isNotBlank(password)) {
            PackageEntity pkg = this.packageService.findByLoginName(name);
            if (pkg != null && pkg.getLoginPassword().equals(password)) {
                request.getSession().setAttribute(PACKAGE_ID, pkg.getId());
                return redirect(request, "/package/report/");
            }
        }
        request.setAttribute("error", "true");
        return render(request, "login");
    }
    @RequestMapping(value = "/report/", method = RequestMethod.GET)
    public String report(HttpServletRequest request, HttpServletResponse response) {
        Long packageId = (Long) request.getSession().getAttribute(PACKAGE_ID);
        if (packageId == null) { return this.redirect(request, "/package/login"); }
        PackageEntity pkg = this.packageService.get(packageId);
        request.setAttribute("pkg", pkg);
        String from = request.getParameter("from");
        String to = request.getParameter("to");
        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtils.isBlank(from)) {
            from = FormatUtil.format(new Date(System.currentTimeMillis() - 20 * 3600 * 24 * 1000));
        }
        params.put("packageId", packageId);
        params.put("from", from);
        if (StringUtils.isNotBlank(to)) params.put("to", to);
        List<PackageStatEntity> statList = this.packageStatService.getPackageSumList(params);
        
        request.setAttribute("statList", statList);
        request.setAttribute("cmpName", packageId);
        request.setAttribute("from", from);
        return render(request, "report");
    }
}
