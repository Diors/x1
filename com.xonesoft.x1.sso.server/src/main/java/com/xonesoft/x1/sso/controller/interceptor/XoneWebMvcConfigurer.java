package com.xonesoft.x1.sso.controller.interceptor;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

public class XoneWebMvcConfigurer extends WebMvcConfigurerAdapter{
	
	  @Override
	    public void addInterceptors(InterceptorRegistry registry) {
	        registry.addInterceptor(new PermissionInterceptor()).addPathPatterns("/**");
	        super.addInterceptors(registry);
	    }
}
