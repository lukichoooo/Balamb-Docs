package com.khundadze.Balamb_Docs;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.javafaker.Faker;
import com.khundadze.Balamb_Docs.dtos.UserRequestDto;
import com.khundadze.Balamb_Docs.services.UserService;

@SpringBootApplication
public class BalambDocsApplication implements CommandLineRunner {

	@Autowired // for testing
	private UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(BalambDocsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Faker faker = new Faker();

		for (int i = 1; i <= 50; i++) {
			UserRequestDto userRequestDto = new UserRequestDto(
					faker.name().firstName(),
					faker.internet().emailAddress(),
					faker.internet().password());

			userService.save(userRequestDto); // save to DB
		}
	}
}
