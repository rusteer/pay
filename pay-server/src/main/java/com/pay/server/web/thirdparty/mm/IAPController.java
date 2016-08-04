package com.pay.server.web.thirdparty.mm;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.pay.server.web.AbstractController;
import com.pay.server.web.thirdparty.mm.bean.SyncAppOrderReq;
import com.pay.server.web.thirdparty.mm.bean.SyncAppOrderResp;

/**
 * MMIAP计费结果回调通知处理类
 * @author kimi
 * @dateTime 2013-4-28 下午4:10:26
 */
@Controller
@RequestMapping("/api/mm")
public class IAPController extends AbstractController {
    protected Logger logger = LoggerFactory.getLogger("mm");
    /**
     * MM计费订单结果通知接口
     * 
     * @author kimi
     * @dateTime 2012-6-18 下午8:21:33
     * @param syncRequest
     * @param request
     * @param response
     * @param model
     * @return 开发者服务器 -> M-Market平台 应答结果
     * @throws Exception
     */
    @RequestMapping(value = "/iap", //
    method = RequestMethod.POST, //
    //consumes = MediaType.APPLICATION_XML_VALUE, //
    produces = MediaType.APPLICATION_XML_VALUE//
    )
    @ResponseBody
    protected ResponseEntity<?> mmiap(@RequestBody SyncAppOrderReq syncRequest, HttpServletRequest request, HttpServletResponse response, Map<String, Object> model) throws Exception {
        if (null == syncRequest || "".equals(syncRequest)) {
            response.setStatus(400);
            return null;
        }
        //log.info(JSONObject.fromObject(result).toString());
        SyncAppOrderResp syncResponse = new SyncAppOrderResp();
        syncResponse.setMsgType("SyncAppOrderResp");
        syncResponse.setVersion("1.0.0");
        syncResponse.sethRet(0);
        try {
            //根据自身项目需要处理计费结果通知信息
            //校验参数...
            //处理过程中，根据MM方提供的响应文档，对hRet设置不同的值进行返回。
            // ......
            //可以自定义一些异常，进行捕获返回响应的值。
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
            syncResponse.sethRet(2);
        }
        logProcess(request, syncRequest, syncResponse);
        return new ResponseEntity<SyncAppOrderResp>(syncResponse, HttpStatus.OK);
    }
    @Override
    protected Logger getLogger() {
        return logger;
    }
}