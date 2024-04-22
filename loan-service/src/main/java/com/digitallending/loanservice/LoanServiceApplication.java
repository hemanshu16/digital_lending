package com.digitallending.loanservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title="Digital Lending BE"))
public class LoanServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(LoanServiceApplication.class, args);
	}
}
