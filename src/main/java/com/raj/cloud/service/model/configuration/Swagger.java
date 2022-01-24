package com.raj.cloud.service.model.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
@Component
@ConfigurationProperties(prefix = "swagger")
@Getter
@Setter
public class Swagger {
	private  String title;
	private  String description;
	private  String version;
	private  String termsOfServiceUrl;
	private  String license;
	private  String licenseUrl;
}
