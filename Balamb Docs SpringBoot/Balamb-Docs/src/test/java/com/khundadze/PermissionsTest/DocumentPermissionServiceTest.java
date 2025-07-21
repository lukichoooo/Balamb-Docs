package com.khundadze.PermissionsTest;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
        private IUserRepository userRepository;

        @Mock
        private DocumentPermissionMapper mapper;

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
        public void createDocumentPermission() throws AccessDeniedException {
                Long documentId = 1L;
                String username = "user";
                Long userId = 2L;
                String role = "viewer";

                // Simulate currently logged in owner
                UserDetails userDetails = org.springframework.security.core.userdetails.User
                                .withUsername("ownerUser").password("pass").roles("USER").build();
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null,
                                userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);

                // Target user
                User user = new User();
                user.setId(userId);
                user.setUsername(username);

                // Document being shared
                Document document = new Document();
                document.setId(documentId);

                // Mock the owner permission
                User ownerUser = new User();
                ownerUser.setUsername("ownerUser");
                DocumentPermission ownerPermission = new DocumentPermission(
                                new DocumentPermissionId(documentId, 99L),
                                document,
                                ownerUser,
                                DocumentRole.OWNER);

                DocumentPermissionId permissionId = new DocumentPermissionId(documentId, userId);
                DocumentPermission expectedPermission = new DocumentPermission(permissionId, document, user,
                                DocumentRole.VIEWER);

                // Mocks
                when(documentRepository.findById(documentId)).thenReturn(Optional.of(document));
                when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
                when(documentPermissionRepository.existsById(permissionId)).thenReturn(false);
                when(documentPermissionRepository.save(any(DocumentPermission.class))).thenReturn(expectedPermission);
                when(documentPermissionRepository.findByDocument_IdAndRole(documentId, DocumentRole.OWNER))
                                .thenReturn(Optional.of(ownerPermission));

                // Call service
                DocumentPermission result = documentPermissionService.createDocumentPermission(documentId, username,
                                role);

                // Assert
                assertEquals(expectedPermission, result);
        }

        @Test
        public void createDocumentPermission_userNotFound() {
                Long documentId = 1L;
                String username = "user";
                String role = "viewer";

                Document document = new Document();
                document.setId(documentId);

                User ownerUser = new User();
                ownerUser.setUsername("ownerUser");

                DocumentPermission ownerPermission = new DocumentPermission(
                                new DocumentPermissionId(documentId, 99L),
                                document,
                                ownerUser,
                                DocumentRole.OWNER);

                // Mock owner authentication
                UserDetails userDetails = org.springframework.security.core.userdetails.User
                                .withUsername("ownerUser").password("pass").roles("USER").build();
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null,
                                userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);

                // Mocks
                when(documentRepository.findById(documentId)).thenReturn(Optional.of(document));
                when(documentPermissionRepository.findByDocument_IdAndRole(documentId, DocumentRole.OWNER))
                                .thenReturn(Optional.of(ownerPermission));
                when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

                // Assert
                UserNotFoundException ex = assertThrows(UserNotFoundException.class,
                                () -> documentPermissionService.createDocumentPermission(documentId, username, role));

                assertEquals("User not found with username: " + username, ex.getMessage());
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
        public void updateDocumentPermission() throws AccessDeniedException {
                Long documentId = 1L;
                String username = "user";
                Long userId = 1L;
                String newRoleStr = "editor";
                DocumentRole newRoleEnum = DocumentRole.EDITOR;

                // Mock target user
                User user = new User();
                user.setId(userId);
                user.setUsername(username);

                // Mock document
                Document document = new Document();
                document.setId(documentId);

                // Mock authenticated owner
                String ownerUsername = "ownerUser";
                UserDetails ownerDetails = org.springframework.security.core.userdetails.User
                                .withUsername(ownerUsername).password("pass").roles("USER").build();
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(ownerDetails, null,
                                ownerDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);

                // Owner permission
                User ownerUser = new User();
                ownerUser.setUsername(ownerUsername);
                DocumentPermission ownerPermission = new DocumentPermission(
                                new DocumentPermissionId(documentId, 999L),
                                document,
                                ownerUser,
                                DocumentRole.OWNER);

                // Target permission
                DocumentPermissionId id = new DocumentPermissionId(documentId, userId);
                DocumentPermission permission = new DocumentPermission(id, document, user, DocumentRole.VIEWER);

                // Mocks
                when(documentPermissionRepository.findByDocument_IdAndRole(documentId, DocumentRole.OWNER))
                                .thenReturn(Optional.of(ownerPermission));
                when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
                when(documentPermissionRepository.findByDocument_IdAndUser_Id(documentId, userId))
                                .thenReturn(Optional.of(permission));
                when(documentPermissionRepository.save(any(DocumentPermission.class)))
                                .thenAnswer(invocation -> invocation.getArgument(0));

                // Execute
                DocumentPermission result = documentPermissionService.updateDocumentPermission(documentId, username,
                                newRoleStr);

                // Assert
                assertEquals(newRoleEnum, result.getRole());
        }

        @Test
        public void deleteByDocumentIdAndUsername() throws AccessDeniedException {
                Long documentId = 1L;
                String usernameToDelete = "userToDelete";

                // Mock authenticated owner user
                String ownerUsername = "ownerUser";
                UserDetails ownerDetails = org.springframework.security.core.userdetails.User
                                .withUsername(ownerUsername).password("pass").roles("USER").build();
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(ownerDetails, null,
                                ownerDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);

                // Mock owner permission
                User ownerUser = new User();
                ownerUser.setUsername(ownerUsername);
                DocumentPermission ownerPermission = new DocumentPermission(
                                new DocumentPermissionId(documentId, 999L),
                                new Document(),
                                ownerUser,
                                DocumentRole.OWNER);

                // Mock repository calls
                when(documentPermissionRepository.findByDocument_IdAndRole(documentId, DocumentRole.OWNER))
                                .thenReturn(Optional.of(ownerPermission));

                User userToDelete = new User();
                userToDelete.setUsername(usernameToDelete);
                when(userRepository.findByUsername(usernameToDelete)).thenReturn(Optional.of(userToDelete));

                // Call the method
                documentPermissionService.deleteByDocumentIdAndUsername(documentId, usernameToDelete);

                // Verify delete was called
                verify(documentPermissionRepository, times(1))
                                .deleteByDocumentIdAndUsername(documentId, usernameToDelete);
        }

        @Test
        public void deleteByDocumentIdAndUsername_cannotRemoveOwner() {
                Long documentId = 1L;
                String ownerUsername = "ownerUser";

                // Mock authenticated owner user
                UserDetails ownerDetails = org.springframework.security.core.userdetails.User
                                .withUsername(ownerUsername).password("pass").roles("USER").build();
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(ownerDetails, null,
                                ownerDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);

                User ownerUser = new User();
                ownerUser.setUsername(ownerUsername);

                DocumentPermission ownerPermission = new DocumentPermission(
                                new DocumentPermissionId(documentId, 999L),
                                new Document(),
                                ownerUser,
                                DocumentRole.OWNER);

                when(documentPermissionRepository.findByDocument_IdAndRole(documentId, DocumentRole.OWNER))
                                .thenReturn(Optional.of(ownerPermission));

                IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                                () -> documentPermissionService.deleteByDocumentIdAndUsername(documentId,
                                                ownerUsername));

                assertEquals("Cannot remove owner permission", ex.getMessage());
        }

        @Test
        public void viwerNotAllowedToChangePermissions() {
                // TODO fix this bug
        }

}
