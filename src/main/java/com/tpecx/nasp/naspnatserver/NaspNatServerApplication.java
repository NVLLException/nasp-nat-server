package com.tpecx.nasp.naspnatserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class NaspNatServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(NaspNatServerApplication.class, args);
	}

}
