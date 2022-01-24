package com.raj.cloud.service.config.swagger;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.raj.cloud.service.model.configuration.Swagger;
import com.raj.cloud.service.model.configuration.SwaggerContact;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

	@Autowired
	Swagger swagger;
	@Autowired
	SwaggerContact swaggerContact;
	
	@Bean
	Docket docket()
	{
		return new Docket(DocumentationType.SWAGGER_2)
				.select().paths(PathSelectors.any())
				.apis(RequestHandlerSelectors.basePackage("com.raj.cloud.service.controller"))
				.build()
				.apiInfo(getApiInfo())
				;
	}

	private ApiInfo getApiInfo() {
		
		return new ApiInfo(swagger.getTitle(),
				swagger.getDescription(),
				swagger.getVersion(), 
				swagger.getTermsOfServiceUrl(),
				new Contact(swaggerContact.getName(),swaggerContact.getUrl(),swaggerContact.getEmail()), 
				swagger.getLicense(), 
				swagger.getLicenseUrl(),
				Collections.emptyList()
				);
	}
	
}
