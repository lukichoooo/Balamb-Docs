package com.khundadze.PermissionsTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
import com.khundadze.Balamb_Docs.services.DocumentPermissionService;

public class DocumentPermissionServiceTest {

    @Mock
    private IDocumentPermissionRepository documentPermissionRepository;

    @Mock
    private IDocumentRepository documentRepository;

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
        DocumentPermission permission = new DocumentPermission(id, null, null, DocumentRole.VIEWER);

        when(documentPermissionRepository.findByDocument_Id(documentId)).thenReturn(List.of(permission));

        List<DocumentRole> result = documentPermissionService.getRolesByDocumentId(documentId);
        List<DocumentRole> expected = List.of(DocumentRole.VIEWER);

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
    public void createDocumentPermission() {
        Long documentId = 1L;
        Long userId = 1L;
        DocumentRole role = DocumentRole.VIEWER;

        User user = new User();
        Document document = new Document();

        DocumentPermissionId id = new DocumentPermissionId(documentId, userId);
        DocumentPermission permission = new DocumentPermission(id, document, user, role);

        when(documentRepository.findById(documentId)).thenReturn(Optional.of(document));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(documentPermissionRepository.save(any(DocumentPermission.class))).thenReturn(permission);

        DocumentPermission result = documentPermissionService.createDocumentPermission(documentId, userId, role);
        DocumentPermission expected = permission;

        assertEquals(expected, result);
    }

    @Test
    public void updateDocumentPermission() {
        Long documentId = 1L;
        Long userId = 1L;
        DocumentRole role = DocumentRole.VIEWER;

        User user = new User();
        Document document = new Document();

        DocumentPermissionId id = new DocumentPermissionId(documentId, userId);
        DocumentPermission permission = new DocumentPermission(id, document, user, role);

        when(documentPermissionRepository.findByDocument_IdAndUser_Id(documentId, userId))
                .thenReturn(Optional.of(permission));

        when(documentPermissionRepository.save(any(DocumentPermission.class)))
                .thenAnswer(invocation -> invocation.getArgument(0)); // return the actual argument we pass to save

        DocumentRole newRole = DocumentRole.EDITOR;

        DocumentPermission result = documentPermissionService.updateDocumentPermission(documentId, userId, newRole);

        assertEquals(newRole, result.getRole());
    }

    @Test
    public void createDocumentPermission_userNotFound() {
        Long documentId = 1L;
        Long userId = 1L;
        DocumentRole role = DocumentRole.VIEWER;

        when(documentRepository.findById(documentId)).thenReturn(Optional.of(new Document()));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        UserNotFoundException ex = assertThrows(UserNotFoundException.class,
                () -> documentPermissionService.createDocumentPermission(documentId, userId, role));

        assertEquals("User not found with id: " + userId, ex.getMessage());
    }

    @Test
    public void createDocumentPermission_documentNotFound() {
        Long documentId = 1L;
        Long userId = 1L;
        DocumentRole role = DocumentRole.VIEWER;

        when(documentRepository.findById(documentId)).thenReturn(Optional.empty());

        DocumentNotFoundException ex = assertThrows(DocumentNotFoundException.class,
                () -> documentPermissionService.createDocumentPermission(documentId, userId, role));

        assertEquals("Document not found with id: " + documentId, ex.getMessage());
    }

}
