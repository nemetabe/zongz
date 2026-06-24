package com.nemetabe.zongz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.nemetabe.zongz")
@EntityScan(basePackages = "com.nemetabe.zongz")
public class ZongzApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZongzApplication.class, args);
	}

}
