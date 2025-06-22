package com.lcwd.hotel.HotelService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// No need of below annotation as after springboot 3 Spring Cloud has removed the need for @EnableEurekaClient altogether.
// @EnableEurekaClient
public class HotelServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelServiceApplication.class, args);
	}

}
