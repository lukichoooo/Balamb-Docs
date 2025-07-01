package com.khundadze.Balamb_Docs.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.khundadze.Balamb_Docs.dtos.DocumentMinimalResponseDto;
import com.khundadze.Balamb_Docs.dtos.DocumentRequestDto;
import com.khundadze.Balamb_Docs.dtos.DocumentResponseDto;
import com.khundadze.Balamb_Docs.exceptions.DocumentNotFoundException;
import com.khundadze.Balamb_Docs.models.Document;
import com.khundadze.Balamb_Docs.repositories.IDocumentRepository;

@Service
public class DocumentService {

    DocumentMapper mapper;
    IDocumentRepository repo;

    public DocumentService(DocumentMapper mapper, IDocumentRepository repo) {
        this.mapper = mapper;
        this.repo = repo;
    }

    public DocumentResponseDto save(DocumentRequestDto requestDocument) {
        Document document = mapper.toDocument(requestDocument);
        return mapper.toDocumentResponseDto(repo.save(document));
    }

    public DocumentResponseDto findById(Long id) {
        Document document = repo.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException("Document not found with id: " + id));

        return mapper.toDocumentResponseDto(document);
    }

    public DocumentResponseDto findByName(String name) {
        Document document = repo.findByName(name)
                .orElseThrow(() -> new DocumentNotFoundException("Document not found with name: " + name));

        return mapper.toDocumentResponseDto(document);
    }

    public List<DocumentMinimalResponseDto> findByNameLike(String name) {
        List<Document> documents = repo.findTop10ByNameStartsWithIgnoreCase(name);
        if (documents.isEmpty()) {
            throw new DocumentNotFoundException("Document not found with name: " + name);
        }

        return documents.stream()
                .map(mapper::toDocumentMinimalResponseDto)
                .toList();
    }

    public List<DocumentResponseDto> getPage(int pageNumber) {
        Page<Document> page = repo.findAll(PageRequest.of(pageNumber, 12));

        return page.getContent().stream()
                .map(mapper::toDocumentResponseDto)
                .toList();
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    public DocumentResponseDto updateContentById(Long id, String content) {
        repo.updateContentById(id, content);
        return findById(id);
    }
}
