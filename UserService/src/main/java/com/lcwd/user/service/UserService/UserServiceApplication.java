package com.lcwd.user.service.UserService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableFeignClients
// For retry below annotation is required
@EnableAspectJAutoProxy(proxyTargetClass = true)
// No need of below annotation as after springboot 3 Spring Cloud has removed the need for @EnableEurekaClient altogether.
// @EnableEurekaClient
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

}
