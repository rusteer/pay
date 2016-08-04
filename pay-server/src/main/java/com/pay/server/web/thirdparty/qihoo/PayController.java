package com.pay.server.web.thirdparty.qihoo;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.pay.server.web.thirdparty.qihoo.msdk.Util;

@Controller
@RequestMapping(value = "/api/qihoo")
public class PayController extends AbstractQihooController {
    public PayController() {
        super();
    }
    private String _errorMsg = "";
    private static final String VERIFY_URL = "http://msdk.mobilem.360.cn/pay/order_verify.json";
    private static final String VERIFIED = "verified";
    @RequestMapping(value = "/paycallback")
    public void doPayCallback(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logRequest(request);
        try {
            response.setContentType("text/html;charset=UTF-8");
            Map<String, String[]> paramterMap = request.getParameterMap();
            HashMap<String, String> params = new HashMap<String, String>();
            String k, v;
            Iterator<String> iterator = paramterMap.keySet().iterator();
            while (iterator.hasNext()) {
                k = iterator.next();
                String arr[] = paramterMap.get(k);
                v = arr[0];
                params.put(k, v);
            }
            String result = processRequest(params);
            this.logProcess(request, null, result);
            write(response, result);
        } catch (Throwable e) {
            this.handleError(request, response, e);
        }
    }
    /**
     * 处理从360过来的支付订单通知请求
     * @param params
     * @return 
     */
    private String processRequest(HashMap<String, String> params) {
        if (!_isValidRequest(params)) {
            if (!_errorMsg.isEmpty()) { return _errorMsg; }
            return "invalid request ";
        }
        if (!_verifyOrder(params)) {
            if (!_errorMsg.isEmpty()) { return _errorMsg; }
            return "verify failed";
        }
        if (isValidOrder(params)) {
            processOrder(params);
        }
        return "ok";
    }
    //处理订单，发货或者增加游戏中的游戏币
    private void processOrder(HashMap<String, String> orderParams) {
        Boolean re = _updateOrder(orderParams);
        if (re) { return; }
        _addCash(orderParams);
    }
    //TODO::更新数据库中的订单状态。
    private Boolean _updateOrder(HashMap<String, String> orderParams) {
        //更新订单,标识为已经处理，避免重复处理
        //如果更新订单状态失败,记录异常，以便再次处理。再次处理的逻辑需应用自己处理
        return true;
    }
    private int _getAmount(HashMap<String, String> orderParams) {
        String isSms = orderParams.get("is_sms");
        int amount = 0;
        if (isSms == null || isSms.equals("") || isSms.equals("0")) {
            String strAmount = orderParams.get("amount");
            amount = Integer.parseInt(strAmount);
        } else {
            //String payExtStr = orderParams.get("pay_ext");
            //TODO::根据consumeCode反推amount
            //json_decode(payExtStr),然后取payExt.get("notify_data").get("consumeCode")
            //TODO::从consumeCode反推amount，注意amount单位为分
        }
        return amount;
    }
    //TODO::发货或者增加游戏中的货币
    private Boolean _addCash(HashMap<String, String> orderParams) {
        //如果发货失败，记录异常，以便再次处理。处理的逻辑需应用自己处理。
        //充值金额，以人民币分为单位。例如2000代表20元
        //int amount = this._getAmount(orderParams);
        //兑换比例(人民币兑换游戏货币，_cashRate==10,表示1元人民币可兑换10游戏货币)
        //int gameCashNum = amount / 100 * this._cashRate;
        return true;
    }
    private Boolean isValidOrder(HashMap<String, String> orderParams) {
        String orderId;
        orderId = orderParams.get("app_order_id");
        if (orderId == null || orderId.equals("")) {
            orderId = orderParams.get("order_id");
        }
        HashMap<String, String> order = _getOrder(orderId);
        if (order == null) { return false; }
        String orderProcessed = order.get("processed");
        if (orderProcessed == null) { return true; }
        if (orderProcessed.equals("")) { return true; }
        return false;
    }
    //TODO::从数据库中获取订单
    private HashMap<String, String> _getOrder(String orderId) {
        HashMap<String, String> order = new HashMap<String, String>();
        order.put("order_id", orderId);
        //该订单是否已经处理过
        //没有处理过
        order.put("processed", "");
        //如果已经处理过，
        //order.put("processed", "1");
        return order;
    }
    /**
     * 向360服务器发起请求验证订单是否有效
     * @param params
     * @return Boolean 是否有效
     */
    private Boolean _verifyOrder(HashMap<String, String> params) {
        String url = VERIFY_URL;
        HashMap<String, String> requestParams = new HashMap<String, String>();
        String field;
        Iterator<String> iterator = params.keySet().iterator();
        while (iterator.hasNext()) {
            field = iterator.next();
            if (field.equals("gateway_flag") || field.equals("sign") || field.contains("sign_return")) {
                continue;
            }
            requestParams.put(field, params.get(field));
        }
        String appKey = params.get("app_key");
        if (!this.secretMap.containsKey(appKey)) {
            _errorMsg = "not my order";
            return false;
        }
        requestParams.put("sign", Util.getSign(requestParams, secretMap.get(appKey)));
        String ret;
        try {
            ret = Util.requestUrl(url, requestParams);
        } catch (IOException e) {
            _errorMsg = e.toString();
            return false;
        } catch (Exception e1) {
            _errorMsg = e1.toString();
            return false;
        }
        JSONParser jsonParser = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
        JSONObject obj;
        try {
            obj = (JSONObject) jsonParser.parse(ret);
            Boolean verified = obj.get("ret").equals(VERIFIED);
            if (!verified) {
                _errorMsg = obj.get("ret").toString();
            }
            return verified;
        } catch (ParseException e) {
            _errorMsg = e.toString();
            return false;
        }
    }
    /**
     * 检查request完整性
     * @param params
     * @return Boolean
     */
    private Boolean _isValidRequest(HashMap<String, String> params) {
        String arrFields[] = { "app_key", "product_id", "app_uid", "order_id", "sign_type", "gateway_flag", "sign", "sign_return", "amount" };
        String value;
        for (String key : arrFields) {
            value = params.get(key);
            if (value == null || value.equals("")) { return false; }
        }
        String appKey = params.get("app_key");
        if (!this.secretMap.containsKey(appKey)) {
            _errorMsg = "not my order";
            return false;
        }
        String sign = Util.getSign(params, secretMap.get(appKey));
        String paramSign = (String) params.get("sign");
        return sign.equals(paramSign);
    }
}
