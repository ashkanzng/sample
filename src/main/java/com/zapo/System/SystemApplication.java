package com.zapo.System;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan({"Controllers","com.zapo.System","Services","DButils"})
@EntityScan("Models")
@EnableJpaRepositories("Repository")
@SpringBootApplication
public class SystemApplication extends SpringBootServletInitializer{
	static Logger LOGGER = LoggerFactory.getLogger("SystemApplication");
	public static void main(String[] args) {
		SpringApplication.run(SystemApplication.class, args);
		LOGGER.info("System is running.........");
	}
}
