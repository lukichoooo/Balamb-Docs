package com.khundadze.SecurityTests;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.khundadze.Balamb_Docs.dtos.DocumentResponseDto;
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

public class Service_SecurityTests {

    private DocumentService documentService;

    private DocumentMapper mapper;

    @Mock
    private IDocumentPermissionRepository documentPermissionRepository;

    @Mock
    private IDocumentRepository documentRepository;

    @Mock
    private IUserRepository userRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mapper = new DocumentMapper(); // Real mapper
        documentService = new DocumentService(mapper, documentRepository, userRepository, documentPermissionRepository);
    }

    @AfterEach
    public void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void userViewDocument() throws AccessDeniedException {
        setViewer();
        Long docId = 1L;

        DocumentResponseDto res = documentService.findById(docId);

        assertEquals(res.id(), docId);
    }

    @Test
    public void userViewDocument_fail() {
        setNoRoles();
        Long docId = 1L;

        assertThrows(AccessDeniedException.class, () -> documentService.findById(docId));
    }

    @Test
    public void userEditDocument_Editor() throws AccessDeniedException {
        setEditor();
        Long docId = 1L;
        String newContent = "test_content";

        documentService.updateContentById(docId, newContent);
    }

    @Test
    public void userEditDocument_Owner() throws AccessDeniedException {
        setOwner();
        Long docId = 1L;
        String newContent = "test_content";

        documentService.updateContentById(docId, newContent);
    }

    @Test
    public void userEditDocument_NoRole_fail() {
        setNoRoles();
        Long docId = 1L;
        String newContent = "test_content";

        assertThrows(AccessDeniedException.class, () -> documentService.updateContentById(docId, newContent));
    }

    @Test
    public void userEditDocument_Viewer_fail() {
        setViewer();
        Long docId = 1L;
        String newContent = "test_content";

        assertThrows(AccessDeniedException.class, () -> documentService.updateContentById(docId, newContent));
    }

    @Test
    public void userDeleteDocument_Owner() throws AccessDeniedException {
        setOwner();
        Long docId = 1L;

        documentService.deleteById(docId);
    }

    @Test
    public void userDeleteDocument_Editor() throws AccessDeniedException {
        setEditor();
        Long docId = 1L;

        assertThrows(AccessDeniedException.class, () -> documentService.deleteById(docId));
    }

    @Test
    public void userDeleteDocument_Viewer_fail() {
        setViewer();
        Long docId = 1L;

        assertThrows(AccessDeniedException.class, () -> documentService.deleteById(docId));
    }

    @Test
    public void userDeleteDocument_NoRole_fail() {
        setNoRoles();
        Long docId = 1L;

        assertThrows(AccessDeniedException.class, () -> documentService.deleteById(docId));
    }

    @Test
    public void userUpdateDescription_Owner() throws AccessDeniedException {
        setOwner();
        Long docId = 1L;
        String newDescription = "test_description";

        documentService.updateDescriptionById(docId, newDescription);
    }

    @Test
    public void userUpdateDescription_Editor() throws AccessDeniedException {
        setEditor();
        Long docId = 1L;
        String newDescription = "test_description";

        documentService.updateDescriptionById(docId, newDescription);
    }

    @Test
    public void userUpdateDescription_Viewer_fail() {
        setViewer();
        Long docId = 1L;
        String newDescription = "test_description";

        assertThrows(AccessDeniedException.class, () -> documentService.updateDescriptionById(docId, newDescription));
    }

    @Test
    public void userUpdateDescription_NoRole_fail() {
        setNoRoles();
        Long docId = 1L;
        String newDescription = "test_description";

        assertThrows(AccessDeniedException.class, () -> documentService.updateDescriptionById(docId, newDescription));
    }

    @Test
    public void userTogglePublic_Owner() throws AccessDeniedException {
        setOwner();
        Long docId = 1L;

        documentService.togglePublic(docId);
    }

    @Test
    public void userTogglePublic_Editor() throws AccessDeniedException {
        setEditor();
        Long docId = 1L;

        assertThrows(AccessDeniedException.class, () -> documentService.togglePublic(docId));
    }

    @Test
    public void userTogglePublic_Viewer_fail() {
        setViewer();
        Long docId = 1L;

        assertThrows(AccessDeniedException.class, () -> documentService.togglePublic(docId));
    }

    @Test
    public void userTogglePublic_NoRole_fail() {
        setNoRoles();
        Long docId = 1L;

        assertThrows(AccessDeniedException.class, () -> documentService.togglePublic(docId));
    }

    @Test
    public void isCurrentUserAllowedToViewDocument_Owner() throws AccessDeniedException {
        setOwner();
        Long docId = 1L;

        assertTrue(documentService.isCurrentUserAllowedToViewDocument(docId));
    }

    @Test
    public void isCurrentUserAllowedToViewDocument_Editor() throws AccessDeniedException {
        setEditor();
        Long docId = 1L;

        assertTrue(documentService.isCurrentUserAllowedToViewDocument(docId));
    }

    @Test
    public void isCurrentUserAllowedToViewDocument_Viewer() throws AccessDeniedException {
        setViewer();
        Long docId = 1L;

        assertTrue(documentService.isCurrentUserAllowedToViewDocument(docId));
    }

    @Test
    public void isCurrentUserAllowedToViewDocument_NoRole_fail() throws AccessDeniedException {
        setNoRoles();
        Long docId = 1L;

        assertThrows(AccessDeniedException.class, () -> documentService.isCurrentUserAllowedToViewDocument(docId));
    }

    // helper methods

    private void setupUserWithRole(DocumentRole role) {
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        Document document = new Document();
        document.setId(1L);

        when(userRepository.findByUsername(user.getUsername()))
                .thenReturn(Optional.of(user));
        when(documentRepository.findById(document.getId())).thenReturn(Optional.of(document));

        if (role != null) {
            when(documentPermissionRepository.findByDocument_IdAndUser_Id(document.getId(), user.getId()))
                    .thenReturn(Optional.of(new DocumentPermission(
                            new DocumentPermissionId(document.getId(), user.getId()),
                            document,
                            user,
                            role)));

            if (role == DocumentRole.OWNER) {
                when(documentPermissionRepository.findByDocument_IdAndRole(document.getId(), DocumentRole.OWNER))
                        .thenReturn(Optional.of(new DocumentPermission(
                                new DocumentPermissionId(document.getId(), user.getId()),
                                document,
                                user,
                                DocumentRole.OWNER)));
            }
        } else {
            when(documentPermissionRepository.findByDocument_IdAndUser_Id(document.getId(), user.getId()))
                    .thenReturn(Optional.empty());
        }

        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(user.getUsername(), null));
    }

    // Specific helpers:
    private void setViewer() {
        setupUserWithRole(DocumentRole.VIEWER);
    }

    private void setEditor() {
        setupUserWithRole(DocumentRole.EDITOR);
    }

    private void setOwner() {
        setupUserWithRole(DocumentRole.OWNER);
    }

    private void setNoRoles() {
        setupUserWithRole(null);
    }
}
