package com.xonesoft.x1.sso.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xonesoft.x1.sso.core.entity.ReturnT;
import com.xonesoft.x1.sso.core.login.SsoTokenLoginHelper;
import com.xonesoft.x1.sso.core.store.SsoLoginStore;
import com.xonesoft.x1.sso.core.store.SsoSessionIdHelper;
import com.xonesoft.x1.sso.core.user.XoneSsoUser;
import com.xonesoft.x1.sso.model.UserInfo;
import com.xonesoft.x1.sso.service.UserService;

@Controller
@RequestMapping("/app")
public class AppController {

	@Autowired
    private UserService userService;


    /**
     * Login
     *
     * @param username
     * @param password
     * @return
     */
    @RequestMapping("/login")
    @ResponseBody
    public ReturnT<String> login(String username, String password) {

        // valid login
        ReturnT<UserInfo> result = userService.findUser(username, password);
        if (result.getCode() != ReturnT.SUCCESS_CODE) {
            return new ReturnT<String>(result.getCode(), result.getMsg());
        }

        // 1、make Xone-sso user
        XoneSsoUser XoneUser = new XoneSsoUser();
        XoneUser.setUserid(String.valueOf(result.getData().getUserid()));
        XoneUser.setUsername(result.getData().getUsername());
        XoneUser.setVersion(UUID.randomUUID().toString().replaceAll("-", ""));
        XoneUser.setExpireMinute(SsoLoginStore.getRedisExpireMinute());
        XoneUser.setExpireFreshTime(System.currentTimeMillis());


        // 2、generate sessionId + storeKey
        String sessionId = SsoSessionIdHelper.makeSessionId(XoneUser);

        // 3、login, store storeKey
        SsoTokenLoginHelper.login(sessionId, XoneUser);

        // 4、return sessionId
        return new ReturnT<String>(sessionId);
    }


    /**
     * Logout
     *
     * @param sessionId
     * @return
     */
    @RequestMapping("/logout")
    @ResponseBody
    public ReturnT<String> logout(String sessionId) {
        // logout, remove storeKey
        SsoTokenLoginHelper.logout(sessionId);
        return ReturnT.SUCCESS;
    }

    /**
     * logincheck
     *
     * @param sessionId
     * @return
     */
    @RequestMapping("/logincheck")
    @ResponseBody
    public ReturnT<XoneSsoUser> logincheck(String sessionId) {

        // logout
        XoneSsoUser XoneUser = SsoTokenLoginHelper.loginCheck(sessionId);
        if (XoneUser == null) {
            return new ReturnT<XoneSsoUser>(ReturnT.FAIL_CODE, "sso not login.");
        }
        return new ReturnT<XoneSsoUser>(XoneUser);
    }
}
