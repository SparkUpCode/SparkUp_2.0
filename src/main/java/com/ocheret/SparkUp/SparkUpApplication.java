package com.ocheret.SparkUp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
//костыль BCryptPasswordEncoder BEAN could not be identified maybe due to file structure
@ComponentScan({"com.ocheret.SparkUp","com.ocheret.SparkUp.security"})
@SpringBootApplication
public class SparkUpApplication {

	public static void main(String[] args) {
		SpringApplication.run(SparkUpApplication.class, args).getBeanDefinitionNames();;
	}

}
