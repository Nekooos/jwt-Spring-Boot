package com.practice.jwtapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.practice.jwtapp")
public class JwtApp {

	public static void main(String[] args) {
		SpringApplication.run(JwtApp.class, args);
	}
}
