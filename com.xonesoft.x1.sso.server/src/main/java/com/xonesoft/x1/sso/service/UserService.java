package com.xonesoft.x1.sso.service;

import com.xonesoft.x1.sso.core.model.UserInfo;
import com.xonesoft.x1.sso.core.result.ReturnT;

public interface UserService {
	 public ReturnT<UserInfo> findUser(String username, String password);
}
