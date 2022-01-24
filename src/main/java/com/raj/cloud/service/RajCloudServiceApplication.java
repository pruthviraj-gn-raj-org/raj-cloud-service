package com.raj.cloud.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class RajCloudServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RajCloudServiceApplication.class, args);
	}

}
