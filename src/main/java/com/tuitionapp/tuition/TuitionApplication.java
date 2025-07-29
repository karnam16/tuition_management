package com.tuitionapp.tuition;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.tuitionapp.tuition"}) 
public class TuitionApplication {

	public static void main(String[] args) {
		SpringApplication.run(TuitionApplication.class, args);
	}

}
