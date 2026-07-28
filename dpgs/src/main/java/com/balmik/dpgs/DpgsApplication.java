package com.balmik.dpgs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DpgsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DpgsApplication.class, args);
	}

}
