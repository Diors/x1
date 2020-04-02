package com.xonesoft.x1.sso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;


@SpringBootApplication
public class XoneSsoApplication extends SpringBootServletInitializer{

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(XoneSsoApplication.class);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(XoneSsoApplication.class, args);
	}

/**
	public static void main(String[] args) {
        SpringApplication.run(XoneSsoApplication.class, args);
	}

**/	
}
