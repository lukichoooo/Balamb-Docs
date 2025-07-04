package com.khundadze.Balamb_Docs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.github.javafaker.Faker;
import com.khundadze.Balamb_Docs.dtos.DocumentRequestDto;
import com.khundadze.Balamb_Docs.dtos.UserRequestDto;
import com.khundadze.Balamb_Docs.services.DocumentService;
import com.khundadze.Balamb_Docs.services.UserService;

@SpringBootApplication
public class BalambDocsApplication implements CommandLineRunner {

	@Autowired // for testing
	private UserService userService;

	@Autowired
	private DocumentService documentService;

	public static void main(String[] args) {
		SpringApplication.run(BalambDocsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Faker faker = new Faker();

		for (int i = 1; i <= 20; i++) {
			UserRequestDto userRequestDto = new UserRequestDto(
					faker.name().firstName(),
					faker.internet().emailAddress(),
					faker.internet().password());
			DocumentRequestDto documentRequestDto = new DocumentRequestDto(
					faker.lorem().sentence(),
					faker.lorem().sentence(),
					faker.lorem().paragraph() + " " +
							faker.lorem().paragraph() + " " +
							faker.lorem().paragraph() + " " +
							faker.lorem().paragraph() + " " +
							faker.lorem().paragraph() + " " +
							faker.lorem().paragraph() + " " +
							faker.lorem().paragraph() + " " +
							faker.lorem().paragraph() + " " +
							faker.lorem().paragraph());

			userService.save(userRequestDto); // save to DB
			documentService.save(documentRequestDto);
		}

		UserRequestDto userRequestDto = new UserRequestDto(
				"lukacho",
				"xundadze@gmail.com",
				"luka12345");
		userService.save(userRequestDto);
	}
}
