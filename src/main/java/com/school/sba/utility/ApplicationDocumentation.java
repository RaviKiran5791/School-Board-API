package com.school.sba.utility;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Configuration
@OpenAPIDefinition
@Builder
@Setter
@Getter
public class ApplicationDocumentation {
	
	Info info() {
		return new Info().title("School-Board-API")
				.version("1.0")
				.description("School Board Project is RESTful API built using Spring-Boot and MySql database")
				.contact(contact());
			
		
	}
	
	Contact contact() {
		return new Contact().name("Ravi Kiran")
				.email("ravikiran@gmail.com")
				.url("abc")
				.email("abcd@gmail.com");
	}
	
	@Bean 
	OpenAPI openaAPI()
	{
		return new OpenAPI().info(info());
	}

}
