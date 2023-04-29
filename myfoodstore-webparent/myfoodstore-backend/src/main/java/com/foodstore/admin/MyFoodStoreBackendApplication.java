package com.foodstore.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan( {"com.foodstore.admin","com.foodstore.admin.*"})
@EnableJpaRepositories(basePackages = {"com.foodstore.admin.*"})
@EntityScan({"com.foodstore.common.*"})
public class MyFoodStoreBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyFoodStoreBackendApplication.class, args);
	}

}
