package com.target.ready.library.system;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.annotation.Validated;

@SpringBootApplication
@EnableTransactionManagement
@Validated
@OpenAPIDefinition(info = @Info(title = "Library Management System API", version = "1.0", description = "Dream School Foundation Digital Library"))
public class LibrarySystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibrarySystemApplication.class, args);

	}

}