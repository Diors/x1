package com.xonesoft.x1.sso.core.store;

import com.xonesoft.x1.sso.core.conf.Conf;
import com.xonesoft.x1.sso.core.user.XoneSsoUser;
import com.xonesoft.x1.sso.core.util.JedisUtil;

public class SsoLoginStore {

	 private static int redisExpireMinute = 1440;    // 1440 minute, 24 hour
	    public static void setRedisExpireMinute(int redisExpireMinute) {
	        if (redisExpireMinute < 30) {
	            redisExpireMinute = 30;
	        }
	        SsoLoginStore.redisExpireMinute = redisExpireMinute;
	    }
	    public static int getRedisExpireMinute() {
	        return redisExpireMinute;
	    }

	    /**
	     * get
	     *
	     * @param storeKey
	     * @return
	     */
	    public static XoneSsoUser get(String storeKey) {

	        String redisKey = redisKey(storeKey);
	        Object objectValue = JedisUtil.getObjectValue(redisKey);
	        if (objectValue != null) {
	            XoneSsoUser XoneUser = (XoneSsoUser) objectValue;
	            return XoneUser;
	        }
	        return null;
	    }

	    /**
	     * remove
	     *
	     * @param storeKey
	     */
	    public static void remove(String storeKey) {
	        String redisKey = redisKey(storeKey);
	        JedisUtil.del(redisKey);
	    }

	    /**
	     * put
	     *
	     * @param storeKey
	     * @param XoneUser
	     */
	    public static void put(String storeKey, XoneSsoUser XoneUser) {
	        String redisKey = redisKey(storeKey);
	        JedisUtil.setObjectValue(redisKey, XoneUser, redisExpireMinute * 60);  // minute to second
	    }

	    private static String redisKey(String sessionId){
	        return Conf.SSO_SESSIONID.concat("#").concat(sessionId);
	    }
}
