package com.pay.server.web.thirdparty.qihoo;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.pay.server.web.thirdparty.qihoo.msdk.QOAuth2;

@Controller
@RequestMapping(value = "/api/qihoo")
public class ApiController extends AbstractQihooController {
    public ApiController() {
        super();
    }
    @RequestMapping(value = "/getinfo")
    public void getInfoByCode(HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            HashMap<String, HashMap<String, Object>> info;
            info = getAuth(request).getInfoByCode(request.getParameter("code"));
            map.put("user", info.get("user"));
            map.put("token", info.get("token"));
            writeResponse(request, response, map);
        } catch (Throwable e) {
            handleError(request, response, e);
        }
    }
    @RequestMapping(value = "/gettoken")
    public void getAccessTokenByCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            writeResponse(request, response, getAuth(request).getAccessTokenByCode(request.getParameter("code"), null));
        } catch (Throwable e) {
            handleError(request, response, e);
        }
    }
    @RequestMapping(value = "/getuser")
    public void getUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            writeResponse(request, response, getAuth(request).userMe(request.getParameter("token")));
        } catch (Throwable e) {
            handleError(request, response, e);
        }
    }
    @RequestMapping(value = "/refreshtoken")
    public void getAccessTokenByRefreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            writeResponse(request, response, getAuth(request).getAccessTokenByRefreshToken(request.getParameter("refresh_token")));
        } catch (Throwable e) {
            handleError(request, response, e);
        }
    }
    @RequestMapping(value = "/gettokeninfo")
    public void getTokenInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HashMap<String, Object> map = null;
        try {
            QOAuth2 auth = getAuth(request);
            if (auth != null) {
                String token = request.getParameter("token");
                if (token != null) {
                    map = auth.getTokenInfo(token);
                }
            }
            writeResponse(request, response, map);
        } catch (Throwable e) {
            handleError(request, response, e);
        }
    }
    protected void writeResponse(HttpServletRequest request, HttpServletResponse response, Map<String, Object> map) {
        if (map != null) {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", "ok");
            result.put("data", map);
            this.writeWithLog(request, response, null,JSONObject.toJSONString(result));
        }
    }
}
