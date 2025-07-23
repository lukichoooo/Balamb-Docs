package com.khundadze.Balamb_Docs.services;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.khundadze.Balamb_Docs.dtos.DocumentMediumResponseDto;
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
import lombok.Getter;
import lombok.Setter;

@Service
@Getter
@Setter
@RequiredArgsConstructor
public class DocumentService {

        private final DocumentMapper mapper;
        private final IDocumentRepository documentRepository;
        private final IUserRepository userRepository;
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

        public DocumentResponseDto findById(Long id) throws AccessDeniedException {

                Document document = documentRepository.findById(id)
                                .orElseThrow(() -> new DocumentNotFoundException("Document not found with id: " + id));

                if (document.isPublic()) {
                        return mapper.toDocumentResponseDto(document);
                }

                assertViewerRole(id);

                return mapper.toDocumentResponseDto(document);
        }

        public DocumentResponseDto findByName(String name) throws AccessDeniedException {
                Document document = documentRepository.findByName(name)
                                .orElseThrow(() -> new DocumentNotFoundException(
                                                "Document not found with name: " + name));

                if (document.isPublic()) {
                        return mapper.toDocumentResponseDto(document);
                }

                assertViewerRole(document.getId());

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

        public List<DocumentMediumResponseDto> getPage(int pageNumber) {
                Page<Document> page = documentRepository.findAll(PageRequest.of(pageNumber, 12));

                return page.getContent().stream()
                                .map(mapper::toDocumentMediumResponseDto)
                                .toList();
        }

        public void deleteById(Long id) throws AccessDeniedException {
                assertOwner(id);

                // delete asociated roles
                documentPermissionRepository.deleteAllByDocumentId(id);

                documentRepository.deleteById(id);
        }

        public DocumentResponseDto updateContentById(Long id, String content) throws AccessDeniedException {
                assertEditorRole(id);

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
                assertEditorRole(id);

                documentRepository.updateDescriptionById(id, description);
                return findById(id);
        }

        public boolean togglePublic(Long id) throws AccessDeniedException {
                assertOwner(id);

                Document document = documentRepository.findById(id)
                                .orElseThrow(() -> new DocumentNotFoundException("Document not found"));

                document.setPublic(!document.isPublic());
                documentRepository.save(document);
                return document.isPublic();
        }

        public boolean isCurrentUserAllowedToViewDocument(Long id) throws AccessDeniedException {
                Document document = documentRepository.findById(id)
                                .orElseThrow(() -> new DocumentNotFoundException("Document not found"));
                if (document.isPublic())
                        return true;

                assertViewerRole(id);
                return true;
        }

        // helper methods
        private void assertOwner(Long documentId) throws AccessDeniedException {
                if (getUserRole(documentId) != DocumentRole.OWNER)
                        throw new AccessDeniedException("Forbidden");
        }

        private void assertEditorRole(Long documentId) throws AccessDeniedException {
                DocumentRole role = getUserRole(documentId);
                if (role != DocumentRole.OWNER && role != DocumentRole.EDITOR)
                        throw new AccessDeniedException("Forbidden");
        }

        private void assertViewerRole(Long documentId) throws AccessDeniedException {
                DocumentRole role = getUserRole(documentId);
                if (role != DocumentRole.OWNER && role != DocumentRole.EDITOR && role != DocumentRole.VIEWER)
                        throw new AccessDeniedException("Forbidden");
        }

        private DocumentRole getUserRole(Long documentId) throws AccessDeniedException {
                String username = SecurityContextHolder.getContext().getAuthentication().getName();
                User user = userRepository.findByUsername(username)
                                .orElseThrow(() -> new UserNotFoundException("User not found"));
                Long userId = user.getId();
                return documentPermissionRepository
                                .findByDocument_IdAndUser_Id(documentId, userId)
                                .orElseThrow(() -> new AccessDeniedException("No permission"))
                                .getRole();
        }

}
