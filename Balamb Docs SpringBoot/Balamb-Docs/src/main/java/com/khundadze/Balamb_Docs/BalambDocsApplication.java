package com.khundadze.Balamb_Docs;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.github.javafaker.Faker;
import com.khundadze.Balamb_Docs.dtos.UserRequestDto;
import com.khundadze.Balamb_Docs.models.Document;
import com.khundadze.Balamb_Docs.models.DocumentPermission;
import com.khundadze.Balamb_Docs.models.DocumentPermissionId;
import com.khundadze.Balamb_Docs.models.DocumentRole;
import com.khundadze.Balamb_Docs.models.User;
import com.khundadze.Balamb_Docs.repositories.IDocumentPermissionRepository;
import com.khundadze.Balamb_Docs.repositories.IDocumentRepository;
import com.khundadze.Balamb_Docs.repositories.IUserRepository;
import com.khundadze.Balamb_Docs.services.UserService;

@SpringBootApplication
public class BalambDocsApplication implements CommandLineRunner {

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private IDocumentRepository documentRepository;

	@Autowired
	private IDocumentPermissionRepository permissionRepository;

	@Autowired
	private UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(BalambDocsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Faker faker = new Faker();

		for (int i = 1; i <= 20; i++) {
			// Create user
			User user = new User();
			user.setUsername(faker.name().firstName());
			user.setPassword(faker.internet().password());
			user.setCreatedAt(LocalDateTime.now());
			user.setUpdatedAt(LocalDateTime.now());
			userRepository.save(user);

			// Create two documents for that user
			for (int j = 0; j < 2; j++) {
				Document doc = new Document();
				doc.setName(faker.lorem().sentence());
				doc.setDescription(faker.lorem().sentence());
				doc.setContent(faker.lorem().paragraph(5));
				doc.setCreatedAt(LocalDateTime.now());
				doc.setUpdatedAt(LocalDateTime.now());
				doc.setPublic(j % 3 == 0);
				documentRepository.save(doc);

				// Give user OWNER permission
				DocumentPermission permission = DocumentPermission.builder()
						.id(new DocumentPermissionId(doc.getId(), user.getId()))
						.user(user)
						.document(doc)
						.role(DocumentRole.OWNER)
						.build();
				permissionRepository.save(permission);
			}
		}

		UserRequestDto user = new UserRequestDto("admin", "password");
		userService.save(user);
	}
}
