package com.pay.server.web.client;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pay.server.bean.Json;
import com.pay.server.bean.PayReport;
import com.pay.server.entity.PackageEntity;
import com.pay.server.entity.PackageInstantEntity;
import com.pay.server.entity.SdkEntity;
import com.pay.server.service.DeviceService;
import com.pay.server.service.PackagePaymentDetailStatService;
import com.pay.server.service.PackagePaymentStatService;
import com.pay.server.service.PackageService;
import com.pay.server.service.PayItemStatService;
import com.pay.server.service.PayReportService;
import com.pay.server.service.SdkService;
import com.pay.server.utils.AES;
import com.pay.server.utils.Utils;

@Controller
@RequestMapping("")
public class ReportController extends AbstractClientController {
    ObjectMapper mapper = new ObjectMapper();
    protected Logger logger = LoggerFactory.getLogger("report");
    @Autowired
    PayReportService reportService;
    @Autowired
    PayItemStatService payItemStatService;
    @Autowired
    PackageService packageService;
    @Autowired
    DeviceService deviceService;
    @Autowired
    SdkService sdkService;
    @Autowired
    PackagePaymentStatService packagePaymentStatService;
    @Autowired
    PackagePaymentDetailStatService packagePaymentDetailStatService;
    @RequestMapping(value = "/api/report")
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject requestObj = null;
        JSONObject responseObj = new JSONObject();
        String time = request.getHeader("time");
        String password = Utils.generatePassword(time);
        JSONObject logObj = new JSONObject();
        if (StringUtils.isNotBlank(time)) {
            try {
                String rawData = request.getParameter("d");
                String data = AES.decode(rawData, password);
                requestObj = new JSONObject(data);
                PackageInstantEntity instant = this.loadPackageInstant(requestObj);
                PackageEntity pkg = this.packageService.get(instant.getPackageId());
                writeClientIds(instant, requestObj, responseObj);
                JSONObject obj = requestObj.optJSONObject("result");
                if (obj != null) {
                    PayReport payReport = Json.optObj(PayReport.class, obj);
                    if (StringUtils.isEmpty(payReport.paySdk)) {
                        payReport.paySdk = "yyyyyyyyy";//20150107以前打包版本中,sdk没有传过来
                    }
                    
                    SdkEntity sdk = sdkService.getByEncodedName(payReport.paySdk);
                    String payInfo = payReport.payInfo;
                    logObj.put("packageInstantId", instant.getId());
                    logObj.put("payReport", new JSONObject(mapper.writeValueAsString(payReport)));
                    String[] fields = payInfo.split(",");
                    int payIndex = Integer.valueOf(fields[0].trim());
                    String payName = fields[1];
                    int price = getPrice(payInfo);
                    logObj.put("price", price);
                    //logger.info("report-price:" + price);
                    reportService.saveReport(instant, payIndex, payName, price, payReport.errorMessage, payReport.result, payReport.paySdk);
                    payItemStatService.increaseStatCount(instant.getId(), payIndex, payName, price, payReport.result);
                    if (payReport.result == 0) {
                        packagePaymentStatService.increasePayCount(pkg, sdk.getId(), price);
                    }
                    {
                        int retentionDays = (int) ((new Date().getTime() - instant.getCreateTime().getTime()) / 1000 / 3600 / 24);
                        int carrierOperator = deviceService.getCarrierOperator(instant.getDeviceId());
                        packagePaymentDetailStatService.increaseCount(pkg, retentionDays, carrierOperator, sdk.getId(), payIndex, payReport.result);
                    }
                }
            } catch (Throwable e) {
                logger.error(e.getMessage(), e);
                responseObj = this.handleError();
            }
        }
        logObj.put("response", responseObj);
        getLogger().info(String.format("%s,%s", getRemoteAddr(request), logObj.toString()));
        this.write(response, AES.encode(responseObj.toString(), password));
    }
    private static int getPrice(String payInfo) {
        String[] fields = payInfo.split(",");
        int result = 0;
        try {
            result = Integer.valueOf(fields[2].trim());
        } catch (Exception e) {}
        if (result == 0) {
            try {
                result = Integer.valueOf(fields[3].trim());
            } catch (Exception e) {}
        }
        return result;
    }
    @Override
    protected Logger getLogger() {
        return logger;
    }
}