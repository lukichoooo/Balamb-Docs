package com.khundadze.Balamb_Docs.services;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.khundadze.Balamb_Docs.dtos.DocumentMinimalResponseDto;
import com.khundadze.Balamb_Docs.dtos.DocumentRequestDto;
import com.khundadze.Balamb_Docs.dtos.DocumentResponseDto;
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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentService { // TODO: add private / public documents

        private final DocumentMapper mapper;
        private final IDocumentRepository documentRepository;
        private final IUserRepository userRepository;
        private final DocumentPermissionService documentPermissionService;
        private final IDocumentPermissionRepository documentPermissionRepository;

        public DocumentResponseDto save(DocumentRequestDto requestDocument) {
                String username = SecurityContextHolder.getContext().getAuthentication().getName();

                User user = userRepository.findByUsername(username)
                                .orElseThrow(() -> new UserNotFoundException("User not found"));

                Document document = mapper.toDocument(requestDocument);
                Document savedDocument = documentRepository.save(document);

                DocumentPermission permission = DocumentPermission.builder()
                                .id(new DocumentPermissionId(savedDocument.getId(), user.getId()))
                                .document(savedDocument)
                                .user(user)
                                .role(DocumentRole.OWNER)
                                .build();

                documentPermissionRepository.save(permission);

                return mapper.toDocumentResponseDto(savedDocument);
        }

        public DocumentResponseDto findById(Long id) {
                Document document = documentRepository.findById(id)
                                .orElseThrow(() -> new DocumentNotFoundException("Document not found with id: " + id));

                return mapper.toDocumentResponseDto(document);
        }

        public DocumentResponseDto findByName(String name) {
                Document document = documentRepository.findByName(name)
                                .orElseThrow(() -> new DocumentNotFoundException(
                                                "Document not found with name: " + name));

                return mapper.toDocumentResponseDto(document);
        }

        public List<DocumentMinimalResponseDto> findByNameLike(String name) {
                List<Document> documents = documentRepository.findTop10ByNameStartsWithIgnoreCase(name);
                if (documents.isEmpty()) {
                        throw new DocumentNotFoundException("Document not found with name: " + name);
                }

                return documents.stream()
                                .map(mapper::toDocumentMinimalResponseDto)
                                .toList();
        }

        public List<DocumentResponseDto> getPage(int pageNumber) {
                Page<Document> page = documentRepository.findAll(PageRequest.of(pageNumber, 12));

                return page.getContent().stream()
                                .map(mapper::toDocumentResponseDto)
                                .toList();
        }

        public void deleteById(Long id) throws AccessDeniedException {
                String username = SecurityContextHolder.getContext().getAuthentication().getName();

                User user = userRepository.findByUsername(username)
                                .orElseThrow(() -> new UserNotFoundException("User not found"));

                assertOwner(id, user.getId());

                // delete asociated roles
                documentPermissionRepository.deleteAllByDocumentId(id);

                documentRepository.deleteById(id);
        }

        public DocumentResponseDto updateContentById(Long id, String content) throws AccessDeniedException {
                String username = SecurityContextHolder.getContext().getAuthentication().getName();

                User user = userRepository.findByUsername(username)
                                .orElseThrow(() -> new UserNotFoundException("User not found"));

                assertEditorOrOwner(id, user.getId());

                documentRepository.updateContentById(id, content);
                return findById(id);
        }

        public List<DocumentMinimalResponseDto> getDocumentsOwnedByUsername(String username) {
                Long userId = userRepository.findByUsername(username)
                                .orElseThrow(() -> new UserNotFoundException("User not found"))
                                .getId();

                List<DocumentPermission> ownedDocumentPermissions = documentPermissionRepository
                                .findByUser_IdAndRole(userId, DocumentRole.OWNER);

                List<Long> documentIds = ownedDocumentPermissions.stream()
                                .map(DocumentPermission::getDocument)
                                .map(Document::getId)
                                .toList();

                if (documentIds.isEmpty()) {
                        return List.of();
                }

                List<Document> ownedDocuments = documentRepository.findAllById(documentIds);

                return ownedDocuments.stream()
                                .map(mapper::toDocumentMinimalResponseDto)
                                .toList();
        }

        public List<DocumentMinimalResponseDto> getDocumentsOwnedByUserId(Long userId) {
                List<DocumentPermission> ownedDocumentPermissions = documentPermissionRepository
                                .findByUser_IdAndRole(userId, DocumentRole.OWNER);

                List<Long> documentIds = ownedDocumentPermissions.stream()
                                .map(DocumentPermission::getDocument)
                                .map(Document::getId)
                                .toList();

                if (documentIds.isEmpty()) {
                        return List.of();
                }

                List<Document> ownedDocuments = documentRepository.findAllById(documentIds);

                return ownedDocuments.stream()
                                .map(mapper::toDocumentMinimalResponseDto)
                                .toList();
        }

        public List<DocumentMinimalResponseDto> getDocumentsByCollaboratorId(Long userId) {
                List<DocumentRole> roles = List.of(DocumentRole.VIEWER, DocumentRole.EDITOR);

                List<DocumentPermission> collaboratorPermissions = documentPermissionRepository
                                .findByUser_IdAndRoleIn(userId, roles);

                List<Long> documentIds = collaboratorPermissions.stream()
                                .map(DocumentPermission::getDocument)
                                .map(Document::getId)
                                .toList();

                if (documentIds.isEmpty()) {
                        return List.of();
                }

                List<Document> documents = documentRepository.findAllById(documentIds);

                return documents.stream()
                                .map(mapper::toDocumentMinimalResponseDto)
                                .toList();
        }

        public DocumentResponseDto updateDescriptionById(Long id, String description) throws AccessDeniedException {
                String username = SecurityContextHolder.getContext().getAuthentication().getName();

                User user = userRepository.findByUsername(username)
                                .orElseThrow(() -> new UserNotFoundException("User not found"));

                assertEditorOrOwner(id, user.getId());

                documentRepository.updateDescriptionById(id, description);
                return findById(id);
        }

        // helper methods
        private void assertEditorOrOwner(Long documentId, Long userId) throws AccessDeniedException {
                DocumentRole role = documentPermissionRepository
                                .findByDocument_IdAndUser_Id(documentId, userId)
                                .orElseThrow(() -> new AccessDeniedException("No permission"))
                                .getRole();

                if (role != DocumentRole.OWNER && role != DocumentRole.EDITOR) {
                        throw new AccessDeniedException("Forbidden");
                }
        }

        private void assertOwner(Long documentId, Long userId) throws AccessDeniedException {
                DocumentRole role = documentPermissionRepository
                                .findByDocument_IdAndUser_Id(documentId, userId)
                                .orElseThrow(() -> new AccessDeniedException("No permission"))
                                .getRole();

                if (role != DocumentRole.OWNER) {
                        throw new AccessDeniedException("Forbidden");
                }
        }

}
