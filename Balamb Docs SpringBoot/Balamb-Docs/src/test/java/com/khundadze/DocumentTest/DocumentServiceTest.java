package com.khundadze.DocumentTest;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.khundadze.Balamb_Docs.dtos.DocumentMediumResponseDto;
import com.khundadze.Balamb_Docs.dtos.DocumentMinimalResponseDto;
import com.khundadze.Balamb_Docs.dtos.DocumentRequestDto;
import com.khundadze.Balamb_Docs.dtos.DocumentResponseDto;
import com.khundadze.Balamb_Docs.exceptions.DocumentNotFoundException;
import com.khundadze.Balamb_Docs.models.Document;
import com.khundadze.Balamb_Docs.models.DocumentPermission;
import com.khundadze.Balamb_Docs.models.DocumentPermissionId;
import com.khundadze.Balamb_Docs.models.DocumentRole;
import com.khundadze.Balamb_Docs.models.User;
import com.khundadze.Balamb_Docs.repositories.IDocumentPermissionRepository;
import com.khundadze.Balamb_Docs.repositories.IDocumentRepository;
import com.khundadze.Balamb_Docs.repositories.IUserRepository;
import com.khundadze.Balamb_Docs.services.DocumentMapper;
import com.khundadze.Balamb_Docs.services.DocumentService;

public class DocumentServiceTest {

        @InjectMocks
        DocumentService documentService;

        @Mock
        DocumentMapper mapper;

        @Mock
        IDocumentRepository documentRepository;

        @Mock
        IUserRepository userRepository;

        @Mock
        IDocumentPermissionRepository documentPermissionRepository;

        @BeforeEach
        public void setUp() {
                MockitoAnnotations.openMocks(this);
                Authentication auth = mock(Authentication.class);
                SecurityContext context = mock(SecurityContext.class);
                when(context.getAuthentication()).thenReturn(auth);
                when(auth.getName()).thenReturn("testuser");
                SecurityContextHolder.setContext(context);
        }

        @Test
        public void save_shouldCreateDocumentAndPermissionAndReturnDto() {
                // Arrange
                DocumentRequestDto requestDto = new DocumentRequestDto("name", "description", "content", false);
                Document document = new Document("name", "description", "content");
                document.setId(1L); // simulate persisted entity

                DocumentResponseDto responseDto = new DocumentResponseDto(1L, "name", "description", "content", false);

                User mockUser = new User();
                mockUser.setId(10L);

                // Mock SecurityContext
                Authentication authentication = mock(Authentication.class);
                SecurityContext securityContext = mock(SecurityContext.class);
                when(securityContext.getAuthentication()).thenReturn(authentication);
                when(authentication.getName()).thenReturn("testuser");
                SecurityContextHolder.setContext(securityContext);

                // Mock behavior
                when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(mockUser));
                when(mapper.toDocument(requestDto)).thenReturn(document);
                when(documentRepository.save(document)).thenReturn(document);
                when(mapper.toDocumentResponseDto(document)).thenReturn(responseDto);

                // Act
                DocumentResponseDto result = documentService.save(requestDto);

                // Assert
                assertEquals(responseDto, result);
                verify(documentPermissionRepository).save(argThat(permission -> permission.getUser().equals(mockUser) &&
                                permission.getDocument().equals(document) &&
                                permission.getRole() == DocumentRole.OWNER));

                SecurityContextHolder.clearContext();
        }

        @Test
        public void findById() throws AccessDeniedException {
                Long id = 1L;
                DocumentResponseDto responseDto = new DocumentResponseDto(1L, "name", "description", "content", false);
                Document document = new Document("name", "description", "content");
                document.setPublic(true);

                when(documentRepository.findById(id)).thenReturn(java.util.Optional.of(document));
                when(mapper.toDocumentResponseDto(document)).thenReturn(responseDto);

                DocumentResponseDto result = documentService.findById(id);

                assertEquals(responseDto, result);
        }

        @Test
        public void findById_notFound() {
                Long id = 1L;

                when(documentRepository.findById(id)).thenReturn(java.util.Optional.empty());

                DocumentNotFoundException ex = assertThrows(DocumentNotFoundException.class,
                                () -> documentService.findById(id));

                assertEquals("Document not found with id: " + id, ex.getMessage());
        }

        @Test
        public void findById_noPermission_shouldThrow() {
                Long id = 1L;
                Document document = new Document("name", "description", "content");
                document.setPublic(false);

                when(documentRepository.findById(id)).thenReturn(Optional.of(document));
                when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(new User()));
                when(documentPermissionRepository.findByDocument_IdAndUser_Id(anyLong(), anyLong()))
                                .thenReturn(Optional.empty()); // No permission

                AccessDeniedException ex = assertThrows(AccessDeniedException.class,
                                () -> documentService.findById(id));
                assertEquals("No permission", ex.getMessage());
        }

        @Test
        public void findByNameLike() {
                String name = "name";
                DocumentMinimalResponseDto responseDto = new DocumentMinimalResponseDto(1L, "name", false);
                Document document = new Document("name", "description", "content");

                when(documentRepository.findTop10ByNameStartsWithIgnoreCase(name)).thenReturn(List.of(document));
                when(mapper.toDocumentMinimalResponseDto(document)).thenReturn(responseDto);

                List<DocumentMinimalResponseDto> result = documentService.findByNameLike(name);

                assertEquals(List.of(responseDto), result);
        }

        @Test
        public void findByNameLike_notFound() {
                String name = "name";

                when(documentRepository.findTop10ByNameStartsWithIgnoreCase(name)).thenReturn(List.of());

                DocumentNotFoundException ex = assertThrows(DocumentNotFoundException.class,
                                () -> documentService.findByNameLike(name));

                assertEquals("Document not found with name: " + name, ex.getMessage());
        }

        @Test
        public void getPage() {
                int pageNumber = 1;
                DocumentMediumResponseDto responseDto = new DocumentMediumResponseDto(1L, "name", "description", false);
                Document document = new Document("name", "description", "content");

                when(documentRepository.findAll(PageRequest.of(pageNumber, 12)))
                                .thenReturn(new PageImpl<>(List.of(document)));
                when(mapper.toDocumentMediumResponseDto(document)).thenReturn(responseDto);

                List<DocumentMediumResponseDto> result = documentService.getPage(pageNumber);

                assertEquals(List.of(responseDto), result);
        }

        @Test
        public void getDocumentsOwnedByUsername() {
                User user = new User();
                user.setId(1L);
                user.setUsername("testuser");

                Document document = new Document();
                document.setId(1L);
                document.setName("Test");
                document.setDescription("Desc");
                document.setContent("Content");

                DocumentPermission permission = new DocumentPermission(
                                new DocumentPermissionId(1L, 1L),
                                document,
                                user,
                                DocumentRole.OWNER);

                when(userRepository.findByUsername("testuser"))
                                .thenReturn(Optional.of(user));
                when(documentPermissionRepository.findByUser_IdAndRole(1L, DocumentRole.OWNER))
                                .thenReturn(List.of(permission));
                when(documentRepository.findAllById(List.of(1L)))
                                .thenReturn(List.of(document));
                when(mapper.toDocumentMinimalResponseDto(document))
                                .thenReturn(new DocumentMinimalResponseDto(1L, "Test", false));

                List<DocumentMinimalResponseDto> result = documentService.getDocumentsOwnedByUsername("testuser");

                assertEquals(1, result.size());
                assertEquals(new DocumentMinimalResponseDto(1L, "Test", false), result.get(0));
        }

        @Test
        public void getDocumentsOwnedByUserId() {
                User user = new User();
                user.setId(1L);

                Document document = new Document();
                document.setId(1L);
                document.setName("Test");
                document.setDescription("Desc");
                document.setContent("Content");

                DocumentPermission permission = new DocumentPermission(
                                new DocumentPermissionId(1L, 1L),
                                document,
                                user,
                                DocumentRole.OWNER);

                when(documentPermissionRepository.findByUser_IdAndRole(1L, DocumentRole.OWNER))
                                .thenReturn(List.of(permission));
                when(documentRepository.findAllById(List.of(1L)))
                                .thenReturn(List.of(document));
                when(mapper.toDocumentMinimalResponseDto(document))
                                .thenReturn(new DocumentMinimalResponseDto(1L, "Test", false));

                List<DocumentMinimalResponseDto> result = documentService.getDocumentsOwnedByUserId(1L);

                assertEquals(1, result.size());
                assertEquals(new DocumentMinimalResponseDto(1L, "Test", false), result.get(0));
        }

        @Test
        public void testGetDocumentsByCollaboratorId() {
                Long userId = 1L;

                User user = new User();
                user.setId(userId);

                Document document = new Document();
                document.setId(2L);
                document.setName("Doc Name");
                document.setDescription("Doc Description");
                document.setContent("Doc Content");

                DocumentPermission permission = new DocumentPermission(
                                new DocumentPermissionId(userId, 2L),
                                document,
                                user,
                                DocumentRole.VIEWER);

                when(documentPermissionRepository.findByUser_IdAndRoleIn(userId,
                                List.of(DocumentRole.VIEWER, DocumentRole.EDITOR)))
                                .thenReturn(List.of(permission));

                when(documentRepository.findAllById(List.of(2L)))
                                .thenReturn(List.of(document));

                when(mapper.toDocumentMinimalResponseDto(document))
                                .thenReturn(new DocumentMinimalResponseDto(2L, "Doc Name", false));

                List<DocumentMinimalResponseDto> result = documentService.getDocumentsByCollaboratorId(userId);

                assertEquals(1, result.size());
                DocumentMinimalResponseDto dto = result.get(0);
                assertEquals(2L, dto.id());
                assertEquals("Doc Name", dto.name());
        }

        @Test
        public void testGetDocumentsByCollaboratorId_emptyList() {
                Long userId = 1L;
                when(documentPermissionRepository.findByUser_IdAndRoleIn(userId,
                                List.of(DocumentRole.VIEWER, DocumentRole.EDITOR)))
                                .thenReturn(List.of());
                List<DocumentMinimalResponseDto> result = documentService.getDocumentsByCollaboratorId(userId);
                assertEquals(true, result.isEmpty());
        }

        @Test
        public void togglePublic_fromPrivate() throws AccessDeniedException {
                Long id = 1L;
                Document document = new Document();
                document.setId(id);
                document.setPublic(false);
                when(documentRepository.findById(id)).thenReturn(Optional.of(document));
                when(documentRepository.save(document)).thenReturn(document);

                when(userRepository.findByUsername("testuser"))
                                .thenReturn(Optional.of(User.builder().id(1L).username("testuser").build()));
                DocumentPermission documentPermission = new DocumentPermission();
                documentPermission.setId(new DocumentPermissionId(id, 1L));
                documentPermission.setDocument(document);
                documentPermission.setUser(User.builder().id(1L).username("testuser").build());
                documentPermission.setRole(DocumentRole.OWNER);
                when(documentPermissionRepository.findByDocument_IdAndUser_Id(id, 1L))
                                .thenReturn(Optional.of(documentPermission));
                Boolean result = documentService.togglePublic(id);
                assertEquals(true, result);
        }

        @Test
        public void togglePublic_fromPublic() throws AccessDeniedException {
                Long id = 1L;
                Document document = new Document();
                document.setId(id);
                document.setPublic(true);
                when(documentRepository.findById(id)).thenReturn(Optional.of(document));
                when(documentRepository.save(document)).thenReturn(document);

                when(userRepository.findByUsername("testuser"))
                                .thenReturn(Optional.of(User.builder().id(1L).username("testuser").build()));
                DocumentPermission documentPermission = new DocumentPermission();
                documentPermission.setId(new DocumentPermissionId(id, 1L));
                documentPermission.setDocument(document);
                documentPermission.setUser(User.builder().id(1L).username("testuser").build());
                documentPermission.setRole(DocumentRole.OWNER);
                when(documentPermissionRepository.findByDocument_IdAndUser_Id(id, 1L))
                                .thenReturn(Optional.of(documentPermission));
                Boolean result = documentService.togglePublic(id);
                assertEquals(false, result);
        }

        @Test
        public void isCurrentUserAllowedToViewDocument() throws AccessDeniedException {
                Document document = new Document();
                document.setPublic(true);
                when(documentRepository.findById(1L)).thenReturn(Optional.of(document));
                boolean result = documentService.isCurrentUserAllowedToViewDocument(1L);
                assertEquals(true, result);
        }

        @Test
        public void isCurrentUserAllowedToViewDocument_private() throws AccessDeniedException {
                Document document = new Document();
                document.setPublic(false);
                when(documentRepository.findById(1L)).thenReturn(Optional.of(document));
                when(userRepository.findByUsername("testuser"))
                                .thenReturn(Optional.of(User.builder().id(1L).username("testuser").build()));
                DocumentPermission documentPermission = new DocumentPermission();
                documentPermission.setId(new DocumentPermissionId(1L, 1L));
                documentPermission.setDocument(document);
                documentPermission.setUser(User.builder().id(1L).username("testuser").build());
                documentPermission.setRole(DocumentRole.OWNER);
                when(documentPermissionRepository.findByDocument_IdAndUser_Id(1L, 1L))
                                .thenReturn(Optional.of(documentPermission));
                boolean result = documentService.isCurrentUserAllowedToViewDocument(1L);
                assertEquals(true, result);
        }

        @Test
        public void isCurrentUserAllowedToViewDocument_private_denied() {
                Document document = new Document();
                document.setPublic(false);
                when(documentRepository.findById(1L)).thenReturn(Optional.of(document));
                when(userRepository.findByUsername("testuser"))
                                .thenReturn(Optional.of(User.builder().id(1L).username("testuser").build()));
                when(documentPermissionRepository.findByDocument_IdAndUser_Id(1L, 1L))
                                .thenReturn(Optional.empty());

                Exception ex = assertThrows(AccessDeniedException.class,
                                () -> documentService.isCurrentUserAllowedToViewDocument(1L));
                assertEquals("No permission", ex.getMessage());
        }
}