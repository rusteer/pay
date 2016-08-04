/**
 * 
 */
package com.pay.server.web.thirdparty.qihoo.msdk;
import java.io.IOException;
import java.util.HashMap;
import org.slf4j.LoggerFactory;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QOAuth2 {
    protected Logger logger = LoggerFactory.getLogger("qihoo");
    private String _clientId = ""; // api key
    private String _clientSecret = ""; // app secret
    private String _scope = "";
    // Set up the API root URL.
    private static final String HOST = "https://openapi.360.cn";
    private static final String ACESSTOKEN_URL = "https://openapi.360.cn/oauth2/access_token";
    private static final String SCOPE_BASIC = "basic";
    private static final String REDIRECT_URL = "oob";
    public QOAuth2(String clientId, String clientSecret, String scope) throws QException {
        if (clientId.isEmpty()) { throw new QException(QException.CODE_NO_APPKEY, ""); }
        if (clientSecret.isEmpty()) { throw new QException(QException.CODE_NO_SECRET, ""); }
        _clientId = clientId;
        _clientSecret = clientSecret;
        if (scope.equals("")) {
            scope = SCOPE_BASIC;
        }
        _scope = scope;
    }
    /**
     * 通过code获得用户信息和token信息(包括access_token, refresh_token)
     * @param code
     * @return
     * @throws QException 
     */
    public HashMap<String, HashMap<String, Object>> getInfoByCode(String code) throws QException {
        if (code == null) { throw new QException(QException.CODE_BAD_PARAM, "需要传code"); }
        HashMap<String, Object> token = getAccessTokenByCode(code, null);
        HashMap<String, Object> user = userMe(token.get("access_token").toString());
        HashMap<String, HashMap<String, Object>> ret = new HashMap<String, HashMap<String, Object>>();
        ret.put("token", token);
        ret.put("user", user);
        return ret;
    }
    /**
     * 通过code换取token(包括access token和 refresh token)
     * @param code
     * @param redirectUri
     * @return
     * @throws QException 
     */
    public HashMap<String, Object> getAccessTokenByCode(String code, String redirectUri) throws QException {
        if (redirectUri == null || redirectUri.isEmpty()) {
            redirectUri = REDIRECT_URL;
        }
        if (code == null) { throw new QException(QException.CODE_BAD_PARAM, "需要传code"); }
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("grant_type", "authorization_code");
        params.put("code", code);
        params.put("client_id", _clientId);
        params.put("client_secret", _clientSecret);
        params.put("redirect_uri", redirectUri);
        params.put("scope", _scope);
        return _request(ACESSTOKEN_URL, params);
    }
    /**
     * 通过access token
     * @param tokenStr
     * @return
     * @throws QException 
     */
    public HashMap<String, Object> userMe(String tokenStr) throws QException {
        if (tokenStr == null) { throw new QException(QException.CODE_BAD_PARAM, "需要传入token"); }
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("access_token", tokenStr);
        return _request(HOST + "/user/me.json", params);
    }
    /**
     * 通过refreshToken刷新得到新的token信息(包括新的access_token和refresh_token)
     * @param refreshToken
     * @return
     * @throws QException 
     */
    public HashMap<String, Object> getAccessTokenByRefreshToken(String refreshToken) throws QException {
        if (refreshToken == null) { throw new QException(QException.CODE_BAD_PARAM, "需要传入refresh_token"); }
        HashMap<String, String> data = new HashMap();
        data.put("grant_type", "refresh_token");
        data.put("refresh_token", refreshToken);
        data.put("client_id", _clientId);
        data.put("client_secret", _clientSecret);
        data.put("scope", _scope);
        return _request(ACESSTOKEN_URL, data);
    }
    /**
     * 查询token信息(app_key 和 360用户id)
     * @param tokenStr access_token
     * @return
     * @throws QException 
     */
    public HashMap<String, Object> getTokenInfo(String tokenStr) throws QException {
        if (tokenStr == null) { throw new QException(QException.CODE_BAD_PARAM, "需要传入token"); }
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("access_token", tokenStr);
        return _request(HOST + "/oauth2/get_token_info.json", params);
    }
    /**
     * 向360服务器发送请求
     * @param url 请求地址
     * @param params 请求参数
     * @return HashMap 请求结果
     * @throws QException 
     */
    private HashMap<String, Object> _request(String url, HashMap<String, String> params) throws QException {
        String fullUrl = url + "?" + Util.httpBuildQuery(params);
        logger.info("requesting 360:" + fullUrl);
        String jsonStr;
        try {
            jsonStr = Util.requestUrl(url, params);
        } catch (IOException e) {
            throw new QException(QException.CODE_NET_ERROR, e.getMessage() + "\r\n" + fullUrl);
        }
        if (jsonStr.isEmpty()) { throw new QException(QException.CODE_NET_ERROR, fullUrl); }
        JSONParser jsonParser = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
        JSONObject obj;
        try {
            obj = (JSONObject) jsonParser.parse(jsonStr);
        } catch (Exception e1) {
            throw new QException(QException.CODE_JSON_ERROR, jsonStr);
        }
        logger.info("360 response:" + obj);
        String errorCode = (String) obj.get("error_code");
        if (errorCode != null && !errorCode.isEmpty()) {
            String err = (String) obj.get("error");
            throw new QException(errorCode, err);
        }
        return obj;
    }
}
