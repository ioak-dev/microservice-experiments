package com.hexagonal.user;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAdminServer
public class UsermanagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsermanagementApplication.class, args);
	}

}