package com.xonesoft.x1.core;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class X1Application extends SpringBootServletInitializer{

	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(X1Application.class);
	}
}
