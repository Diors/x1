package com.xonesoft.x1.sso.core.store;

import com.xonesoft.x1.sso.core.user.XoneSsoUser;

public class SsoSessionIdHelper {

	/**
	 * make client sessionId
	 *
	 * @param xxlSsoUser
	 * @return
	 */
	public static String makeSessionId(XoneSsoUser xxlSsoUser) {
		String sessionId = xxlSsoUser.getUserid().concat("_").concat(xxlSsoUser.getVersion());
		return sessionId;
	}

	/**
	 * parse storeKey from sessionId
	 *
	 * @param sessionId
	 * @return
	 */
	public static String parseStoreKey(String sessionId) {
		if (sessionId != null && sessionId.indexOf("_") > -1) {
			String[] sessionIdArr = sessionId.split("_");
			if (sessionIdArr.length == 2 && sessionIdArr[0] != null && sessionIdArr[0].trim().length() > 0) {
				String userId = sessionIdArr[0].trim();
				return userId;
			}
		}
		return null;
	}

	/**
	 * parse version from sessionId
	 *
	 * @param sessionId
	 * @return
	 */
	public static String parseVersion(String sessionId) {
		if (sessionId != null && sessionId.indexOf("_") > -1) {
			String[] sessionIdArr = sessionId.split("_");
			if (sessionIdArr.length == 2 && sessionIdArr[1] != null && sessionIdArr[1].trim().length() > 0) {
				String version = sessionIdArr[1].trim();
				return version;
			}
		}
		return null;
	}
}
