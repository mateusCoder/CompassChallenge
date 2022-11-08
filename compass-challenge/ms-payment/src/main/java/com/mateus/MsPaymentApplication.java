package com.mateus;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EnableRabbit
public class MsPaymentApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsPaymentApplication.class, args);
	}

}
