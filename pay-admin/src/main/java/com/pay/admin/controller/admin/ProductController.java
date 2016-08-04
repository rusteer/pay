package com.pay.admin.controller.admin;
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
import com.pay.admin.entity.ProductEntity;
import com.pay.admin.entity.ProductPayItemEntity;
import com.pay.admin.service.ProductPayItemService;
import com.pay.admin.service.ProductService;

@Controller
@RequestMapping("/admin/" + ProductController.cmpName)
public class ProductController extends AbstractController {
    public static final String cmpName = "product";
    @Autowired
    private ProductService service;
    @Autowired
    private ProductPayItemService productPayItemService;
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
            List<ProductPayItemEntity> items = this.productPayItemService.findByProductId(entity.getId());
            for (ProductPayItemEntity item : items) {
                sb.append(item.getPayIndex()).append(",").append(item.getPayName()).append(",").append(item.getPayPrice()).append("\n");
            }
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
            savePayItems(request, entity.getId());
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
    private void savePayItems(HttpServletRequest request, Long productId) {
        String payInfo = request.getParameter("payItems");
        if (StringUtils.isNotBlank(payInfo)) {
            Map<Integer,ProductPayItemEntity> map=new HashMap<Integer,ProductPayItemEntity>();
            String[] lines = payInfo.split("\n");
            for (String line : lines) {
                if (StringUtils.isNotBlank(line)) {
                    String[] fields = line.split(",");
                    if (fields.length >= 3) {
                        ProductPayItemEntity item = new ProductPayItemEntity();
                        item.setPayIndex(Integer.valueOf(fields[0].trim()));
                        item.setPayName(fields[1].trim());
                        item.setPayPrice(Integer.valueOf(fields[2].trim()));
                        item.setProductId(productId);
                        map.put(item.getPayIndex(), item);
                       
                    }
                }
            }
            productPayItemService.saveOrUpdate (productId,map);
        }
    }
}
