package com.lideragro.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.lideragro.api.config.properties.LiderAgroProperties;

@SpringBootApplication
@EnableConfigurationProperties(LiderAgroProperties.class)
public class LideragroApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(LideragroApiApplication.class, args);
	}
}
