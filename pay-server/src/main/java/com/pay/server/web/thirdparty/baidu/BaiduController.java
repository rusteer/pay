package com.pay.server.web.thirdparty.baidu;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.pay.server.web.AbstractController;

/**
 * MMIAP计费结果回调通知处理类
 * @author kimi
 * @dateTime 2013-4-28 下午4:10:26
 */
@Controller
@RequestMapping("/api/baidu")
public class BaiduController extends AbstractController {
    protected Logger logger = LoggerFactory.getLogger("baidu");
    @RequestMapping(value = "/querypayresult")
    protected void queryPayResult(HttpServletRequest request, HttpServletResponse response) {
        StringBuilder result = new StringBuilder();
        StringBuilder error = new StringBuilder();
        BufferedReader bf = null;
        try {
            // 构造参数
            String transdata = "{\"appid\":" + Constants.appid + ",\"exorderno\":\"1385614772792\"}";
            String sign = Md5Util.getMD5((transdata + Constants.appkey).getBytes(), false);
            String param = "r=FromIapppayToUserAction&transdata=" + transdata + "&sign=" + sign;
            logger.info(param);
            URL url = new URL("http://gameopen.baidu.com/index.php");
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            // 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在
            // http正文内，因此需要设为true, 默认情况下是false;
            urlConn.setDoOutput(true);
            // 设置是否从httpUrlConnection读入，默认情况下是true;
            urlConn.setDoInput(true);
            // Post 请求不能使用缓存
            urlConn.setUseCaches(false);
            // 设定传送的内容类型是可序列化的java对象
            // (如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException)
            urlConn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
            urlConn.setRequestProperty("Connection", "Keep-Alive");
            // 设定请求的方法为"POST"，默认是GET
            urlConn.setRequestMethod("POST");
            // 连接，上面对urlConn的所有配置必须要在connect之前完成，
            urlConn.connect();
            // 此处getOutputStream会隐含的进行connect (即：如同调用上面的connect()方法，
            // 所以在开发中不调用上述的connect()也可以)。
            OutputStream outStrm = urlConn.getOutputStream();
            // 向对象输出流写出数据，这些数据将存到内存缓冲区中
            outStrm.write(param.getBytes());
            // 调用HttpURLConnection连接对象的getInputStream()函数,
            // 将内存缓冲区中封装好的完整的HTTP请求电文发送到服务端。
            int respCode = urlConn.getResponseCode();
            if (200 == respCode) {
                bf = new BufferedReader(new InputStreamReader(urlConn.getInputStream()), 1000);
                String str = null;
                while ((str = bf.readLine()) != null) {
                    result.append(str);
                }
            } else {
                bf = new BufferedReader(new InputStreamReader(urlConn.getErrorStream()), 1000);
                String str = null;
                while ((str = bf.readLine()) != null) {
                    error.append(str);
                }
            }
        } catch (Throwable e) {
            getLogger().error(e.getMessage(), e);
        }
        try {
            if (bf != null) bf.close();
        } catch (IOException e) {
            getLogger().error(e.getMessage(), e);
        }
        if (error.length() > 0) {
            getLogger().error(error.toString());
        }
        this.logProcess(request, null, result);
        this.write(response, result);
    }
    @RequestMapping(value = "/paynotify")
    public void payNotify(HttpServletRequest request, HttpServletResponse response) {
        String result = "fail";
        String transdata = request.getParameter("transdata");
        String sign = request.getParameter("sign");
        logger.info(transdata);
        if (sign.equals(Md5Util.getMD5((transdata + Constants.appkey).getBytes(), false))) {
            //给支付服务器应答
            result = "SUCCESS";
        }
        this.logProcess(request, null, result);
    }
    @RequestMapping(value = "/getinfo")
    public void getUserInfo(HttpServletRequest request, HttpServletResponse response) {
        BufferedReader reader = null;
        StringBuilder result = new StringBuilder();
        StringBuilder error = new StringBuilder();
        try {
            // 构造请求参数
            String transdata = "{\"appid\":\"1314690\",\"code\":\"1381906308416194-10031-94148\"}";
            String param = "r=FromIapppayToUserAction&m=domethod2&transdata=" + transdata + "&sign=" + Md5Util.getMD5((transdata + Constants.appkey).getBytes(), false);
            URL url = new URL("http://gameopen.baidu.com/index.php");
            logger.info(param);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            // 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在
            // http正文内，因此需要设为true, 默认情况下是false;
            urlConn.setDoOutput(true);
            // 设置是否从httpUrlConnection读入，默认情况下是true;
            urlConn.setDoInput(true);
            // Post 请求不能使用缓存
            urlConn.setUseCaches(false);
            // 设定传送的内容类型是可序列化的java对象
            // (如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException)
            urlConn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
            urlConn.setRequestProperty("Connection", "Keep-Alive");
            // 设定请求的方法为"POST"，默认是GET
            urlConn.setRequestMethod("POST");
            // 连接，上面对urlConn的所有配置必须要在connect之前完成，
            urlConn.connect();
            // 此处getOutputStream会隐含的进行connect (即：如同调用上面的connect()方法，
            // 所以在开发中不调用上述的connect()也可以)。
            OutputStream outStrm = urlConn.getOutputStream();
            // 向对象输出流写出数据，这些数据将存到内存缓冲区中
            outStrm.write(param.getBytes());
            // 调用HttpURLConnection连接对象的getInputStream()函数,
            // 将内存缓冲区中封装好的完整的HTTP请求电文发送到服务端。
            int respCode = urlConn.getResponseCode();
            if (200 == respCode) {
                reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()), 1000);
                String str = null;
                while ((str = reader.readLine()) != null) {
                    result.append(str);
                }
            } else {
                reader = new BufferedReader(new InputStreamReader(urlConn.getErrorStream()), 1000);
                String str = null;
                while ((str = reader.readLine()) != null) {
                    error.append(str);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        try {
            if (reader != null) reader.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        if (error.length() > 0) {
            logger.error(error.toString());
        } else {
            this.logProcess(request, null, result);
        }
        this.write(response, result);
    }
    @Override
    protected Logger getLogger() {
        return logger;
    }
}