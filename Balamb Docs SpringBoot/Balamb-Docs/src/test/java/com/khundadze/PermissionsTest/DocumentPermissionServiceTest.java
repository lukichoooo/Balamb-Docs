package com.khundadze.PermissionsTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.khundadze.Balamb_Docs.dtos.DocumentPermissionUserRoleDto;
import com.khundadze.Balamb_Docs.exceptions.DocumentNotFoundException;
import com.khundadze.Balamb_Docs.exceptions.UserNotFoundException;
import com.khundadze.Balamb_Docs.models.Document;
import com.khundadze.Balamb_Docs.models.DocumentPermission;
import com.khundadze.Balamb_Docs.models.DocumentPermissionId;
import com.khundadze.Balamb_Docs.models.DocumentRole;
import com.khundadze.Balamb_Docs.models.User;
import com.khundadze.Balamb_Docs.repositories.IDocumentPermissionRepository;
import com.khundadze.Balamb_Docs.repositories.IDocumentRepository;
import com.khundadze.Balamb_Docs.repositories.IUserRepository;
import com.khundadze.Balamb_Docs.services.DocumentPermissionMapper;
import com.khundadze.Balamb_Docs.services.DocumentPermissionService;

public class DocumentPermissionServiceTest {

        @Mock
        private IDocumentPermissionRepository documentPermissionRepository;

        @Mock
        private IDocumentRepository documentRepository;

        @Mock
        private DocumentPermissionMapper mapper;

        @Mock
        private IUserRepository userRepository;

        @InjectMocks
        private DocumentPermissionService documentPermissionService;

        @BeforeEach
        public void setUp() {
                MockitoAnnotations.openMocks(this);
        }

        @Test
        public void getRolesByDocumentId() {
                Long documentId = 1L;
                DocumentPermissionId id = new DocumentPermissionId(documentId, 1L);
                User user = new User();
                user.setId(1L);
                DocumentPermission permission = new DocumentPermission(id, null, user, DocumentRole.VIEWER);

                when(documentPermissionRepository.findByDocument_Id(documentId)).thenReturn(List.of(permission));
                when(mapper.toDto(permission))
                                .thenReturn(new DocumentPermissionUserRoleDto(user.getUsername(), DocumentRole.VIEWER));

                List<DocumentPermissionUserRoleDto> result = documentPermissionService.getRolesByDocumentId(documentId);
                List<DocumentPermissionUserRoleDto> expected = List
                                .of(new DocumentPermissionUserRoleDto(user.getUsername(), DocumentRole.VIEWER));

                assertEquals(expected, result);
        }

        @Test
        public void getRolesByUserId() {
                Long userId = 1L;
                DocumentPermissionId id = new DocumentPermissionId(1L, userId);
                DocumentPermission permission = new DocumentPermission(id, null, null, DocumentRole.VIEWER);

                when(documentPermissionRepository.findByUser_Id(userId)).thenReturn(List.of(permission));

                List<DocumentRole> result = documentPermissionService.getRolesByUserId(userId);
                List<DocumentRole> expected = List.of(DocumentRole.VIEWER);

                assertEquals(expected, result);
        }

        @Test
        public void createDocumentPermission_documentNotFound() {
                Long documentId = 1L;
                String username = "user";
                String role = "viewer";

                // Owner auth still required even though document fails early
                UserDetails userDetails = org.springframework.security.core.userdetails.User
                                .withUsername("ownerUser").password("pass").roles("USER").build();
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null,
                                userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);

                when(documentRepository.findById(documentId)).thenReturn(Optional.empty());

                DocumentNotFoundException ex = assertThrows(DocumentNotFoundException.class,
                                () -> documentPermissionService.createDocumentPermission(documentId, username, role));

                assertEquals("Document not found with id: " + documentId, ex.getMessage());
        }

        @Test
        public void getPermission_throwsIfNotFound() {
                Long docId = 1L, userId = 2L;

                when(documentPermissionRepository.findByDocument_IdAndUser_Id(docId, userId))
                                .thenReturn(Optional.empty());

                assertThrows(RuntimeException.class, () -> documentPermissionService.getPermission(docId, userId));
        }

        @Test
        public void createDocumentPermission_userNotFound() {
                when(documentRepository.findById(1L)).thenReturn(Optional.of(new Document()));
                when(userRepository.findByUsername("missing")).thenReturn(Optional.empty());

                assertThrows(UserNotFoundException.class,
                                () -> documentPermissionService.createDocumentPermission(1L, "missing", "viewer"));
        }

}
