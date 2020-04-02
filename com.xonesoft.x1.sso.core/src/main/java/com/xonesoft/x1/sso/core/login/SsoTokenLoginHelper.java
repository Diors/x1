package com.xonesoft.x1.sso.core.login;

import javax.servlet.http.HttpServletRequest;

import com.xonesoft.x1.sso.core.conf.Conf;
import com.xonesoft.x1.sso.core.store.SsoLoginStore;
import com.xonesoft.x1.sso.core.store.SsoSessionIdHelper;
import com.xonesoft.x1.sso.core.user.XoneSsoUser;

public class SsoTokenLoginHelper {
	   /**
     * client login
     *
     * @param sessionId
     * @param XoneUser
     */
    public static void login(String sessionId, XoneSsoUser XoneUser) {

        String storeKey = SsoSessionIdHelper.parseStoreKey(sessionId);
        if (storeKey == null) {
            throw new RuntimeException("parseStoreKey Fail, sessionId:" + sessionId);
        }

        SsoLoginStore.put(storeKey, XoneUser);
    }

    /**
     * client logout
     *
     * @param sessionId
     */
    public static void logout(String sessionId) {

        String storeKey = SsoSessionIdHelper.parseStoreKey(sessionId);
        if (storeKey == null) {
            return;
        }

        SsoLoginStore.remove(storeKey);
    }
    /**
     * client logout
     *
     * @param request
     */
    public static void logout(HttpServletRequest request) {
        String headerSessionId = request.getHeader(Conf.SSO_SESSIONID);
        logout(headerSessionId);
    }


    /**
     * login check
     *
     * @param sessionId
     * @return
     */
    public static XoneSsoUser loginCheck(String  sessionId){

        String storeKey = SsoSessionIdHelper.parseStoreKey(sessionId);
        if (storeKey == null) {
            return null;
        }

        XoneSsoUser XoneUser = SsoLoginStore.get(storeKey);
        if (XoneUser != null) {
            String version = SsoSessionIdHelper.parseVersion(sessionId);
            if (XoneUser.getVersion().equals(version)) {

                // After the expiration time has passed half, Auto refresh
                if ((System.currentTimeMillis() - XoneUser.getExpireFreshTime()) > XoneUser.getExpireMinute()/2) {
                    XoneUser.setExpireFreshTime(System.currentTimeMillis());
                    SsoLoginStore.put(storeKey, XoneUser);
                }

                return XoneUser;
            }
        }
        return null;
    }


    /**
     * login check
     *
     * @param request
     * @return
     */
    public static XoneSsoUser loginCheck(HttpServletRequest request){
        String headerSessionId = request.getHeader(Conf.SSO_SESSIONID);
        return loginCheck(headerSessionId);
    }

}
