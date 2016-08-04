package com.pay.admin.controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.pay.admin.service.framework.AccountService;

/**
 * LoginController负责打开登录页面(GET请求)和登录出错页面(POST请求)，
 * <p/>
 * 真正登录的POST请求由Filter完成,
 *
 * @author calvin
 */
@Controller
@RequestMapping(value = "/account")
public class AccountController {
    @Autowired
    AccountService accountService;
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String fail(@RequestParam(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM) String userName, Model model) {
        model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, userName);
        return "account/login";
    }
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(HttpServletRequest request, HttpServletResponse response) {
        return "account/login";
    }
}
