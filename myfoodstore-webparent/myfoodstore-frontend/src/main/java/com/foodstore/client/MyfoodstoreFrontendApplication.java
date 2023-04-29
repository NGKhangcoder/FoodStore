package com.foodstore.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan( {"com.foodstore.client.*","com.foodstore.client"})
@EnableJpaRepositories(basePackages = {"com.foodstore.client.*"})
@EntityScan({"com.foodstore.common.*"})
public class MyfoodstoreFrontendApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyfoodstoreFrontendApplication.class, args);
	}

}
