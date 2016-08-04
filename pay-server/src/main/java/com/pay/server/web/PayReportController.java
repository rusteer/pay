package com.pay.server.web;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/payreport")
public class PayReportController extends AbstractController {
    protected Logger logger = LoggerFactory.getLogger("payreport");
    @RequestMapping(value = "")
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.writeWithLog(request, response, null,"ok");
    }
    @Override
    protected Logger getLogger() {
        return logger;
    }
}