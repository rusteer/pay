package com.pay.server.web;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.zip.GZIPOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONStyle;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

public abstract class AbstractController {
    protected abstract Logger getLogger();
    ObjectMapper mapper = new ObjectMapper();
    final static int BUFFER_SIZE = 4096;
    protected void write(HttpServletResponse response, Object obj) {
        if (obj != null) {
            PrintWriter out = null;
            try {
                out = new PrintWriter(response.getOutputStream());
                String content = obj.toString();
                out.write(content);
            } catch (IOException e) {
                getLogger().error(e.getMessage(), e);
            } finally {
                if (out != null) out.close();
            }
        }
    }
    protected String getParameters(HttpServletRequest request) {
        /*JSONObject obj = new JSONObject();
        Map<String, String[]> map = request.getParameterMap();
        for (String key : map.keySet()) {
            String[] values = map.get(key);
            if (values != null && values.length > 0) {
                try {
                    if (values.length == 1) {
                        obj.put(key, values[0]);
                    } else {
                        JSONArray array = new JSONArray();
                        for (String value : values) {
                            array.put(value);
                        }
                        obj.put(key, array);
                    }
                } catch (Throwable e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }*/
        return JSONObject.toJSONString(request.getParameterMap(), JSONStyle.MAX_COMPRESS);
    }
    protected void logRequest(HttpServletRequest request) {
        getLogger().info(getRemoteAddr(request) + "," + getRequestPath(request) + "," + getParameters(request) + "\n");
    }
    @SuppressWarnings("deprecation")
    protected String getRequestPath(HttpServletRequest request) {
        String requestPath = request.getHeader("REQUEST_PATH");
        if (StringUtils.isBlank(requestPath)) {
            String queryString = request.getQueryString();
            StringBuffer buf = javax.servlet.http.HttpUtils.getRequestURL(request);
            if (StringUtils.isNoneBlank(queryString)) {
                buf.append("?").append(queryString);
            }
            requestPath = buf.toString();
        }
        return requestPath;
    }
    protected String getRemoteAddr(HttpServletRequest request) {
        String result = request.getHeader("X-Real-IP");
        if (StringUtils.isBlank(result)) {
            result = request.getRemoteAddr();
        }
        return result;
    }
    protected void zipWrite(HttpServletResponse response, Object obj) {
        if (obj != null) {
            PrintWriter out = null;
            response.setHeader("Content-Encoding", "gzip");
            try {
                out = new PrintWriter(new GZIPOutputStream(response.getOutputStream()));
                String content = obj.toString();
                out.write(content);
            } catch (IOException e) {
                getLogger().error(e.getMessage(), e);
            } finally {
                if (out != null) out.close();
            }
        }
    }
    protected int getIntParameter(HttpServletRequest request, String name, int defaultValue) {
        int result = defaultValue;
        String value = request.getParameter(name);
        if (StringUtils.isNotBlank(value)) {
            try {
                result = Integer.valueOf(request.getParameter(name));
            } catch (Exception e) {}
        }
        return result;
    }
    protected int getIntParameter(HttpServletRequest request, String name) {
        int result = 0;
        try {
            result = Integer.valueOf(request.getParameter(name));
        } catch (Exception e) {}
        return result;
    }
    protected long getLongParameter(HttpServletRequest request, String name) {
        long result = 0;
        try {
            result = Long.valueOf(request.getParameter(name));
        } catch (Exception e) {}
        return result;
    }
    protected void logProcess(HttpServletRequest request, Object requestObj, Object responseObj) {
        ReqestResponse rr = new ReqestResponse();
        rr.requestObj = requestObj;
        rr.responseObj = responseObj;
        rr.ip = this.getRemoteAddr(request);
        rr.path = this.getRequestPath(request);
        //rr.params = request.getParameterMap();
        request.getParameterMap();
        getLogger().info(new Gson().toJson(rr) + "\n");
    }
    protected void writeWithLog(HttpServletRequest request, HttpServletResponse response,Object requestObj, Object responseObj) {
        if (responseObj == null) responseObj = "";
        this.logProcess(request, requestObj, responseObj);
        this.write(response, responseObj);
    }
    class ReqestResponse {
        private String ip;
        private String path;
        private Object requestObj;
        private Object responseObj;
        private Map<String, String[]> params;
        public Object getRequestObj() {
            return requestObj;
        }
        public Object getResponseObj() {
            return responseObj;
        }
        public void setRequestObj(Object requestObj) {
            this.requestObj = requestObj;
        }
        public void setResponseObj(Object responseObj) {
            this.responseObj = responseObj;
        }
        public String getIp() {
            return ip;
        }
        public String getPath() {
            return path;
        }
        public void setIp(String ip) {
            this.ip = ip;
        }
        public void setPath(String path) {
            this.path = path;
        }
        public Map<String, String[]> getParams() {
            return params;
        }
        public void setParams(Map<String, String[]> params) {
            this.params = params;
        }
    }
}
