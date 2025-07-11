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
import com.khundadze.Balamb_Docs.models.DocumentRole;
import com.khundadze.Balamb_Docs.models.User;
import com.khundadze.Balamb_Docs.repositories.IDocumentPermissionRepository;
import com.khundadze.Balamb_Docs.repositories.IDocumentRepository;
import com.khundadze.Balamb_Docs.repositories.IUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentMapper mapper;
    private final IDocumentRepository documentRepository;
    private final IUserRepository userRepository;
    private final IDocumentPermissionRepository documentPermissionRepository;

    public DocumentResponseDto save(DocumentRequestDto requestDocument) {
        Document document = mapper.toDocument(requestDocument);
        return mapper.toDocumentResponseDto(documentRepository.save(document));
    }

    public DocumentResponseDto findById(Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException("Document not found with id: " + id));

        return mapper.toDocumentResponseDto(document);
    }

    public DocumentResponseDto findByName(String name) {
        Document document = documentRepository.findByName(name)
                .orElseThrow(() -> new DocumentNotFoundException("Document not found with name: " + name));

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

    public void deleteById(Long id) {
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

    // helper methods
    private void assertEditorOrOwner(Long documentId, Long userId) throws AccessDeniedException {
        DocumentRole role = documentPermissionRepository
                .findByDocument_IdAndUser_Id(documentId, userId)
                .orElseThrow(() -> new RuntimeException("No permission"))
                .getRole();

        if (role != DocumentRole.OWNER && role != DocumentRole.EDITOR) {
            throw new AccessDeniedException("Forbidden");
        }
    }

}
