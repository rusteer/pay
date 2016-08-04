package com.pay.admin.controller.admin;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.pay.admin.controller.WebUtil;
import com.pay.admin.entity.ChannelEntity;
import com.pay.admin.service.ChannelService;

@Controller
@RequestMapping("/admin/" + ChannelController.cmpName)
public class ChannelController extends AbstractController {
    public static final String cmpName = "channel";
    @Autowired
    ChannelService service;
    private ChannelEntity composeEntity(HttpServletRequest request) {
        ChannelEntity entity = new ChannelEntity();
        entity.setId(WebUtil.getLongParameter(request, "id"));
        entity.setDisplayName(request.getParameter("displayName"));
        entity.setLoginName(request.getParameter("loginName"));
        entity.setPassword(request.getParameter("password"));
        entity.setHot(WebUtil.getIntParameter(request, "hot"));
        entity.setShowPayCount("1".equals(request.getParameter("showPayCount")));
        entity.setShowPayEarning("1".equals(request.getParameter("showPayEarning")));
        entity.setShowActivateCount("1".equals(request.getParameter("showActivateCount")));
        entity.setAutoSync("1".equals(request.getParameter("autoSync")));
        entity.setDiscountRate(WebUtil.getIntParameter(request, "discountRate"));
        entity.setInfo(request.getParameter("info"));
        if (entity.getId() == 0) {
            entity.setCreateTime(new Date());
        }
        return entity;
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String form(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response) {
        ChannelEntity entity = id > 0 ? service.get(id) : new ChannelEntity(id);
        loadData(request, entity);
        return render(request, "form");
    }
    @Override
    protected String getCmpName() {
        return cmpName;
    }
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String list(HttpServletRequest request, HttpServletResponse response) {
        List<ChannelEntity> list = service.getListOrderByHot();
        request.setAttribute("list", list);
        return render(request, "list");
    }
    public void loadData(HttpServletRequest request, ChannelEntity entity) {
        request.setAttribute("entity", entity);
    }
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String save(HttpServletRequest request, HttpServletResponse response) {
        ChannelEntity entity = composeEntity(request);
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
