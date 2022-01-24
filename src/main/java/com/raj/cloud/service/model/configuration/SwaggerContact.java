package com.raj.cloud.service.model.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "swagger.contact")
public class SwaggerContact {
	  private String name;
	  private String url;
	  private String email;
	}