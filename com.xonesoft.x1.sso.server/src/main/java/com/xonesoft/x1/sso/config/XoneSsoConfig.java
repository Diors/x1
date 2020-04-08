package com.xonesoft.x1.sso.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.xonesoft.x1.sso.core.store.SsoLoginStore;
import com.xonesoft.x1.sso.core.util.JedisUtil;


@Configuration
public class XoneSsoConfig {
	//@Value("${xone.sso.redis.address}")
    private String redisAddress="redis://127.0.0.1:6379";

    //@Value("${xone.sso.redis.expire.minute}")
    private int redisExpireMinute=60;

    public void afterPropertiesSet() throws Exception {
        SsoLoginStore.setRedisExpireMinute(redisExpireMinute);
        JedisUtil.init(redisAddress);
    }

    public void destroy() throws Exception {
        JedisUtil.close();
    }
}
