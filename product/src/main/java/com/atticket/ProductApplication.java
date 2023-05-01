package com.atticket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

import com.atticket.common.response.BaseExceptionHandler;

@SpringBootApplication
@EnableFeignClients
@Import(BaseExceptionHandler.class)
public class ProductApplication {
	public static void main(String[] args) {
		SpringApplication.run(ProductApplication.class, args);
	}
}
