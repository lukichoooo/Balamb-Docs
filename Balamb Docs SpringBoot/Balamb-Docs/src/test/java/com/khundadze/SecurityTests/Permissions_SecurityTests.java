package com.khundadze.SecurityTests;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

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

public class Permissions_SecurityTests {

    @InjectMocks
    private DocumentPermissionService documentPermissionsService;

    @Mock
    private IDocumentPermissionRepository documentPermissionRepository;

    @Mock
    private IDocumentRepository documentRepository;

    @Mock
    private IUserRepository userRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void viewerNotAllowedToChangePermissions() {
        setViewer();

        assertThrows(AccessDeniedException.class,
                () -> documentPermissionsService.updateDocumentPermission(1L, "testUser", "EDITOR"));
    }

    @Test
    public void editorAllowedToChangePermissions() throws AccessDeniedException {
        setEditor();

        documentPermissionsService.updateDocumentPermission(1L, "testUser", "EDITOR");
    }

    @Test
    public void ownerCantChangeTheirOwnPermissions() throws AccessDeniedException {
        setOwner();

        assertThrows(AccessDeniedException.class,
                () -> documentPermissionsService.updateDocumentPermission(1L, "testUser", "OWNER"));
    }

    @Test
    public void ownerChangeTargetUsersPermission_Success() throws AccessDeniedException {
        setupUserWithRole(DocumentRole.OWNER);

        createAndMockTargetViewer(2L, "targetViewer");
        documentPermissionsService.updateDocumentPermission(1L, "targetViewer", "EDITOR");

        createAndMockTargetEditor(3L, "targetEditor");
        documentPermissionsService.updateDocumentPermission(1L, "targetEditor", "VIEWER");
    }

    @Test
    public void ownerDeleteTargetUsersPermission_Success() throws AccessDeniedException {
        setupUserWithRole(DocumentRole.OWNER);

        createAndMockTargetViewer(2L, "targetViewer");
        documentPermissionsService.deleteByDocumentIdAndUsername(1L, "targetViewer");

        createAndMockTargetEditor(3L, "targetEditor");
        documentPermissionsService.deleteByDocumentIdAndUsername(1L, "targetEditor");
    }

    @Test
    public void editorDeletePermission_Fail() throws AccessDeniedException {
        setupUserWithRole(DocumentRole.EDITOR);
        assertThrows(AccessDeniedException.class,
                () -> documentPermissionsService.deleteByDocumentIdAndUsername(1L, "testUser"));
    }

    @Test
    public void viewerDeletePermission_Fail() throws AccessDeniedException {
        setupUserWithRole(DocumentRole.VIEWER);
        assertThrows(AccessDeniedException.class,
                () -> documentPermissionsService.deleteByDocumentIdAndUsername(1L, "testUser"));
    }

    @Test
    public void changePermissionForNonExistentUser_ShouldThrow() {
        setupUserWithRole(DocumentRole.OWNER);

        // userRepository returns empty for target username
        when(userRepository.findByUsername("nonExistentUser")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> documentPermissionsService.updateDocumentPermission(1L, "nonExistentUser", "EDITOR"));
    }

    @Test
    public void changePermissionForNonExistentDocument_ShouldThrow() {
        setupUserWithRole(DocumentRole.OWNER);
        createAndMockTargetViewer(2L, "targetUser");

        // documentRepository returns empty for document id
        when(documentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(DocumentNotFoundException.class,
                () -> documentPermissionsService.updateDocumentPermission(999L, "targetUser", "EDITOR"));
    }

    @Test
    public void createDocumentPermission_duplicate() {
        setupUserWithRole(DocumentRole.OWNER); // the current user must be OWNER to create permissions

        Document document = new Document();
        document.setId(1L);

        User targetUser = new User();
        targetUser.setId(5L);
        targetUser.setUsername("alice");

        DocumentPermissionId id = new DocumentPermissionId(1L, 5L);

        when(documentRepository.findById(1L)).thenReturn(Optional.of(document));
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(targetUser));
        when(documentPermissionRepository.existsById(id)).thenReturn(true);

        assertThrows(
                com.khundadze.Balamb_Docs.exceptions.DuplicatePermissionException.class,
                () -> documentPermissionsService.createDocumentPermission(1L, "alice", "viewer"));
    }

    @Test
    public void createDocumentPermission_success() throws Exception {
        setupUserWithRole(DocumentRole.OWNER); // current user must be OWNER

        Document document = new Document();
        document.setId(1L);

        User targetUser = new User();
        targetUser.setId(5L);
        targetUser.setUsername("bob");

        DocumentPermissionId id = new DocumentPermissionId(1L, 5L);

        when(documentRepository.findById(1L)).thenReturn(Optional.of(document));
        when(userRepository.findByUsername("bob")).thenReturn(Optional.of(targetUser));
        when(documentPermissionRepository.existsById(id)).thenReturn(false);

        DocumentPermission newPermission = new DocumentPermission(id, document, targetUser, DocumentRole.VIEWER);
        when(documentPermissionRepository.save(any())).thenReturn(newPermission);

        DocumentPermission result = documentPermissionsService.createDocumentPermission(1L, "bob", "viewer");

        assert (result != null);
        assert (result.getRole() == DocumentRole.VIEWER);
        assert (result.getUser().getId() == 5L);
    }

    // helper methods
    private User createAndMockTargetViewer(Long id, String username) {
        return createAndMockTargetUserWithRole(id, username, DocumentRole.VIEWER);
    }

    private User createAndMockTargetEditor(Long id, String username) {
        return createAndMockTargetUserWithRole(id, username, DocumentRole.EDITOR);
    }

    private User createAndMockTargetUserWithRole(Long id, String username, DocumentRole role) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);

        Document document = new Document();
        document.setId(1L);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(documentPermissionRepository.findByDocument_IdAndUser_Id(document.getId(), user.getId()))
                .thenReturn(Optional.of(new DocumentPermission(
                        new DocumentPermissionId(document.getId(), user.getId()),
                        document,
                        user,
                        role)));
        when(documentRepository.findById(document.getId())).thenReturn(Optional.of(document));
        when(documentRepository.save(document)).thenReturn(document);

        return user;
    }

    private void setupUserWithRole(DocumentRole role) {
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        Document document = new Document();
        document.setId(1L);

        when(userRepository.findByUsername(user.getUsername()))
                .thenReturn(Optional.of(user));

        when(documentPermissionRepository.findByDocument_IdAndUser_Id(document.getId(), user.getId()))
                .thenReturn(Optional.of(new DocumentPermission(
                        new DocumentPermissionId(document.getId(), user.getId()),
                        document,
                        user,
                        role)));

        when(documentRepository.findById(1L)).thenReturn(Optional.of(new Document()));

        if (role == DocumentRole.OWNER) {
            when(documentPermissionRepository.findByDocument_IdAndRole(document.getId(), DocumentRole.OWNER))
                    .thenReturn(Optional.of(new DocumentPermission(
                            new DocumentPermissionId(document.getId(), user.getId()),
                            document,
                            user,
                            DocumentRole.OWNER)));
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

}
