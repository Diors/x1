package com.xonesoft.x1.sso.service;

import com.xonesoft.x1.sso.core.entity.ReturnT;
import com.xonesoft.x1.sso.model.UserInfo;

public interface UserService {
	 public ReturnT<UserInfo> findUser(String username, String password);
}
