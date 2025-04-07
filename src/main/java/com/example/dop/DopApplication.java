package com.example.dop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.example.dop.Model")
@EnableJpaRepositories(basePackages = "com.example.dop.Repository")
@ComponentScan(basePackages = "com.example.dop")
public class DopApplication {

	public static void main(String[] args) {
		SpringApplication.run(DopApplication.class, args);
	}

}
