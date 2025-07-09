package com.khundadze.Balamb_Docs.services;

import java.util.List;

import org.springframework.stereotype.Service;

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

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentPermissionService {

    private final IDocumentPermissionRepository documentPermissionRepository;
    private final IDocumentRepository documentRepository;
    private final IUserRepository userRepository;

    public List<DocumentRole> getRolesByDocumentId(Long documentId) {
        List<DocumentPermission> permissions = documentPermissionRepository.findByDocument_Id(documentId);
        return permissions.stream()
                .map(DocumentPermission::getRole)
                .toList();
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

    public DocumentPermission createDocumentPermission(Long documentId, Long userId, DocumentRole role) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new DocumentNotFoundException("Document not found with id: " + documentId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        DocumentPermissionId id = new DocumentPermissionId(documentId, userId);
        DocumentPermission permission = new DocumentPermission(id, document, user, role);

        return documentPermissionRepository.save(permission);
    }

    @Transactional
    public DocumentPermission updateDocumentPermission(Long documentId, Long userId, DocumentRole role) {
        DocumentPermission permission = getPermission(documentId, userId);
        permission.setRole(role);
        return permission; // Hibernate flushes automatically on transaction commit
    }

}
