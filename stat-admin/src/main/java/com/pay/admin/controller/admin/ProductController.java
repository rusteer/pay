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
import com.pay.admin.entity.ProductEntity;
import com.pay.admin.service.ProductService;

@Controller
@RequestMapping("/admin/" + ProductController.cmpName)
public class ProductController extends AbstractController {
    public static final String cmpName = "product";
    @Autowired
    private ProductService service;
    private ProductEntity composeEntity(HttpServletRequest request) {
        ProductEntity entity = new ProductEntity();
        entity.setId(WebUtil.getLongParameter(request, "id"));
        entity.setHot(WebUtil.getIntParameter(request, "hot"));
        entity.setName(request.getParameter("name"));
        entity.setPackageName(request.getParameter("packageName"));
        entity.setDescription(request.getParameter("description"));
        entity.setCodePath(request.getParameter("codePath"));
        entity.setProjectName(request.getParameter("projectName"));
        if (entity.getId() == 0) {
            entity.setCreateTime(new Date());
        }
        return entity;
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String form(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response) {
        ProductEntity entity = id > 0 ? service.get(id) : new ProductEntity(id);
        loadData(request, entity);
        return render(request, "form");
    }
    @Override
    protected String getCmpName() {
        return cmpName;
    }
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String list(HttpServletRequest request, HttpServletResponse response) {
        List<ProductEntity> list = service.getListOrderByHot();
        request.setAttribute("list", list);
        return render(request, "list");
    }
    public void loadData(HttpServletRequest request, ProductEntity entity) {
        if (entity.getId() > 0) {
            StringBuilder sb = new StringBuilder();
            
            request.setAttribute("payItems", sb.toString());
        }
        request.setAttribute("entity", entity);
    }
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String save(HttpServletRequest request, HttpServletResponse response) {
        ProductEntity entity = composeEntity(request);
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
