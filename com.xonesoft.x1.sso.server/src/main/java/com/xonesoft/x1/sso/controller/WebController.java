package com.xonesoft.x1.sso.controller;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.xonesoft.x1.sso.core.conf.Conf;
import com.xonesoft.x1.sso.core.login.SsoWebLoginHelper;
import com.xonesoft.x1.sso.core.model.UserInfo;
import com.xonesoft.x1.sso.core.result.ReturnT;
import com.xonesoft.x1.sso.core.store.SsoLoginStore;
import com.xonesoft.x1.sso.core.store.SsoSessionIdHelper;
import com.xonesoft.x1.sso.core.user.XoneSsoUser;
import com.xonesoft.x1.sso.service.UserService;

@Controller
public class WebController {
	
	@Autowired
    private UserService userService;

    @RequestMapping("/")
    public String index(Model model, HttpServletRequest request, HttpServletResponse response) {

        // login check
        XoneSsoUser XoneUser = SsoWebLoginHelper.loginCheck(request, response);

        if (XoneUser == null) {
            return "redirect:/login";
        } else {
            model.addAttribute("XoneUser", XoneUser);
            return "index";
        }
    }

    /**
     * Login page
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(Conf.SSO_LOGIN)
    public String login(Model model, HttpServletRequest request, HttpServletResponse response) {

        // login check
        XoneSsoUser XoneUser = SsoWebLoginHelper.loginCheck(request, response);

        if (XoneUser != null) {

            // success redirect
            String redirectUrl = request.getParameter(Conf.REDIRECT_URL);
            if (redirectUrl!=null && redirectUrl.trim().length()>0) {

                String sessionId = SsoWebLoginHelper.getSessionIdByCookie(request);
                String redirectUrlFinal = redirectUrl + "?" + Conf.SSO_SESSIONID + "=" + sessionId;;

                return "redirect:" + redirectUrlFinal;
            } else {
                return "redirect:/";
            }
        }

        model.addAttribute("errorMsg", request.getParameter("errorMsg"));
        model.addAttribute(Conf.REDIRECT_URL, request.getParameter(Conf.REDIRECT_URL));
        return "login";
    }

    /**
     * Login
     *
     * @param request
     * @param redirectAttributes
     * @param username
     * @param password
     * @return
     */
    @RequestMapping("/doLogin")
    public String doLogin(HttpServletRequest request,
                        HttpServletResponse response,
                        RedirectAttributes redirectAttributes,
                        String username,
                        String password,
                        String ifRemember) {

        boolean ifRem = (ifRemember!=null&&"on".equals(ifRemember))?true:false;

        // valid login
        ReturnT<UserInfo> result = userService.findUser(username, password);
        if (result.getCode() != ReturnT.SUCCESS_CODE) {
            redirectAttributes.addAttribute("errorMsg", result.getMsg());

            redirectAttributes.addAttribute(Conf.REDIRECT_URL, request.getParameter(Conf.REDIRECT_URL));
            return "redirect:/login";
        }

        // 1、make Xone-sso user
        XoneSsoUser XoneUser = new XoneSsoUser();
        XoneUser.setUserid(String.valueOf(result.getData().getUserid()));
        XoneUser.setUsername(result.getData().getUsername());
        XoneUser.setVersion(UUID.randomUUID().toString().replaceAll("-", ""));
        XoneUser.setExpireMinute(SsoLoginStore.getRedisExpireMinute());
        XoneUser.setExpireFreshTime(System.currentTimeMillis());


        // 2、make session id
        String sessionId = SsoSessionIdHelper.makeSessionId(XoneUser);

        // 3、login, store storeKey + cookie sessionId
        SsoWebLoginHelper.login(response, sessionId, XoneUser, ifRem);

        // 4、return, redirect sessionId
        String redirectUrl = request.getParameter(Conf.REDIRECT_URL);
        if (redirectUrl!=null && redirectUrl.trim().length()>0) {
            String redirectUrlFinal = redirectUrl + "?" + Conf.SSO_SESSIONID + "=" + sessionId;
            return "redirect:" + redirectUrlFinal;
        } else {
            return "redirect:/";
        }

    }

    /**
     * Logout
     *
     * @param request
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(Conf.SSO_LOGOUT)
    public String logout(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {

        // logout
        SsoWebLoginHelper.logout(request, response);

        redirectAttributes.addAttribute(Conf.REDIRECT_URL, request.getParameter(Conf.REDIRECT_URL));
        return "redirect:/login";
    }
}
