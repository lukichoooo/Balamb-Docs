package com.khundadze.Balamb_Docs.services;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.khundadze.Balamb_Docs.dtos.DocumentPermissionUserRoleDto;
import com.khundadze.Balamb_Docs.exceptions.DocumentNotFoundException;
import com.khundadze.Balamb_Docs.exceptions.DuplicatePermissionException;
import com.khundadze.Balamb_Docs.exceptions.InvalidRoleException;
import com.khundadze.Balamb_Docs.exceptions.UserNotFoundException;
import com.khundadze.Balamb_Docs.models.Document;
import com.khundadze.Balamb_Docs.models.DocumentPermission;
import com.khundadze.Balamb_Docs.models.DocumentPermissionId;
import com.khundadze.Balamb_Docs.models.DocumentRole;
import com.khundadze.Balamb_Docs.models.User;
import com.khundadze.Balamb_Docs.repositories.IDocumentPermissionRepository;
import com.khundadze.Balamb_Docs.repositories.IDocumentRepository;
import com.khundadze.Balamb_Docs.repositories.IUserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentPermissionService {

    private final IDocumentPermissionRepository documentPermissionRepository;
    private final IDocumentRepository documentRepository;
    private final IUserRepository userRepository;
    private final DocumentPermissionMapper mapper;

    public List<DocumentPermissionUserRoleDto> getRolesByDocumentId(Long documentId) {
        List<DocumentPermission> permissions = documentPermissionRepository.findByDocument_Id(documentId);
        return permissions.stream().map(mapper::toDto).toList();
    }

    public List<DocumentRole> getRolesByUserId(Long userId) {
        List<DocumentPermission> permissions = documentPermissionRepository.findByUser_Id(userId);
        return permissions.stream()
                .map(DocumentPermission::getRole)
                .toList();
    }

    public DocumentPermission getPermission(Long documentId, Long userId) {
        return documentPermissionRepository.findByDocument_IdAndUser_Id(documentId, userId)
                .orElseThrow(() -> new RuntimeException("Permission not found"));
    }

    public List<DocumentPermission> getOwnedByUserId(Long userId) {
        return documentPermissionRepository.findByUser_IdAndRole(userId, DocumentRole.OWNER);
    }

    public DocumentPermission createDocumentPermission(Long documentId, String username, String role)
            throws AccessDeniedException {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new DocumentNotFoundException("Document not found with id: " + documentId));

        checkOwnerPermission(documentId);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        DocumentPermissionId id = new DocumentPermissionId(documentId, user.getId());

        if (documentPermissionRepository.existsById(id)) {
            throw new DuplicatePermissionException("Permission already exists for this user and document");
        }

        DocumentRole roleEnum;
        try {
            roleEnum = DocumentRole.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidRoleException("Invalid role: " + role + ". Allowed values: EDITOR, VIEWER");
        }

        DocumentPermission permission = new DocumentPermission(id, document, user, roleEnum);
        return documentPermissionRepository.save(permission);
    }

    @Transactional
    public DocumentPermission updateDocumentPermission(Long documentId, String username, String role)
            throws AccessDeniedException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        DocumentPermission permission = getPermission(documentId, user.getId());

        // Prevent changing OWNER role
        if (permission.getRole() == DocumentRole.OWNER) {
            throw new AccessDeniedException("Cannot change role of the owner");
        }

        DocumentRole roleEnum;
        try {
            roleEnum = DocumentRole.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidRoleException("Invalid role: " + role);
        }

        permission.setRole(roleEnum);
        return permission; // Hibernate auto flushes on transaction commit
    }

    @Transactional
    public void deleteByDocumentIdAndUsername(Long documentId, String username) throws AccessDeniedException {
        checkOwnerPermission(documentId);

        Optional<DocumentPermission> ownerPermissionOpt = documentPermissionRepository
                .findByDocument_IdAndRole(documentId, DocumentRole.OWNER);

        if (ownerPermissionOpt.isEmpty()) {
            throw new IllegalStateException("Document has no owner permission");
        }

        String ownerUsername = ownerPermissionOpt.get().getUser().getUsername();

        if (ownerUsername.equals(username)) {
            throw new IllegalArgumentException("Cannot remove owner permission");
        }

        if (!userRepository.findByUsername(username).isPresent()) {
            throw new UserNotFoundException("User not found with username: " + username);
        }

        documentPermissionRepository.deleteByDocumentIdAndUsername(documentId, username);
    }

    // helper methods
    private void checkOwnerPermission(Long documentId) throws AccessDeniedException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUsername = (principal instanceof UserDetails)
                ? ((UserDetails) principal).getUsername()
                : principal.toString();

        Optional<DocumentPermission> ownerPermissionOpt = documentPermissionRepository
                .findByDocument_IdAndRole(documentId, DocumentRole.OWNER);

        if (ownerPermissionOpt.isEmpty() ||
                !ownerPermissionOpt.get().getUser().getUsername().equals(currentUsername)) {
            throw new AccessDeniedException("Only the owner can modify permissions for this document");
        }
    }

}
