package com.vroomvroom.safemobis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SafeMobisApplication {

	public static void main(String[] args) {
		SpringApplication.run(SafeMobisApplication.class, args);
	}

}
