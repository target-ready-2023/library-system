package com.target.ready.library.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class LibrarySystemApplication {


	public static void main(String[] args) {
		SpringApplication.run(LibrarySystemApplication.class, args);

	}

}