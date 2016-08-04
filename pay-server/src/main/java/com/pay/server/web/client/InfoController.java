package com.pay.server.web.client;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.pay.server.entity.PackageEntity;
import com.pay.server.entity.PackageInstantEntity;
import com.pay.server.entity.PackagePayItemEntity;
import com.pay.server.entity.SdkEntity;
import com.pay.server.service.PackagePayItemService;
import com.pay.server.service.SdkService;
import com.pay.server.utils.AES;
import com.pay.server.utils.SdkConstants;
import com.pay.server.utils.Utils;

@Controller
@RequestMapping("")
public class InfoController extends AbstractClientController {
    @Autowired
    private PackagePayItemService packagePayItemService;
    @Autowired
    private SdkService sdkService;
    protected Logger logger = LoggerFactory.getLogger("info");
    private static final String ON_LINE_PARAMS_KEY = "params";
    private static final String WEB_CONFIG_SDK_KEY = "C";
    private static final String PAY_ITEM_CONFIG_KEY = "pItems";
    @RequestMapping(value = "/api/info")
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject requestObj = null;
        JSONObject responseObj = new JSONObject();
        String time = request.getHeader("time");
        String password = Utils.generatePassword(time);
        if (StringUtils.isNotBlank(time)) {
            try {
                String rawData = request.getParameter("d");
                String data = AES.decode(rawData, password);
                requestObj = new JSONObject(data);
                PackageInstantEntity instant = this.loadPackageInstant(requestObj);
                writeClientIds(instant, requestObj, responseObj);
                JSONObject onLineParams = new JSONObject();
                PackageEntity pkg = packagService.get(instant.getPackageId());
                String paySdk = pkg.getPaySdk();
                if (StringUtils.isNotEmpty(paySdk)) {
                    onLineParams.put(WEB_CONFIG_SDK_KEY, SdkConstants.getEncodedSdk(paySdk));
                }
                JSONArray payItems = loadPayItems(pkg);
                if (payItems.length() > 0) {
                    responseObj.put(PAY_ITEM_CONFIG_KEY, payItems);
                }
                responseObj.put(ON_LINE_PARAMS_KEY, onLineParams);
            } catch (Throwable e) {
                logger.error(e.getMessage(), e);
                responseObj = this.handleError();
            }
        }
        this.response(request, response, requestObj, responseObj, password);
    }
    private JSONArray loadPayItems(PackageEntity pkg) {
        List<PackagePayItemEntity> payItems = packagePayItemService.findByPackageId(pkg.getId());
        Map<Long, SdkEntity> sdkMap = sdkService.getMapAll();
        JSONArray array = new JSONArray();
        for (PackagePayItemEntity entity : payItems) {
            JSONObject obj = new JSONObject();
            obj.put("pi", entity.getPayIndex());
            {
                Long sdk1Id = entity.getPaySdk1();
                if (sdk1Id != null && sdk1Id > 0) {
                    obj.put("s1", sdkMap.get(sdk1Id).getEncodedName());
                    obj.put("p1", entity.getPaySdk1Price());
                }
            }
            {
                Long sdk2Id = entity.getPaySdk2();
                if (sdk2Id != null && sdk2Id > 0) {
                    obj.put("s2", sdkMap.get(sdk2Id).getEncodedName());
                    obj.put("p2", entity.getPaySdk2Price());
                }
            }
            {
                Long sdk3Id = entity.getPaySdk3();
                if (sdk3Id != null && sdk3Id > 0) {
                    obj.put("s3", sdkMap.get(sdk3Id).getEncodedName());
                    obj.put("p3", entity.getPaySdk3Price());
                }
            }
            array.put(obj);
        }
        return array;
    }
    @Override
    protected Logger getLogger() {
        return logger;
    }
}