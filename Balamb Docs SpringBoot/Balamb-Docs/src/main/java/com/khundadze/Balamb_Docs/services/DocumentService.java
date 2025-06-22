package com.khundadze.Balamb_Docs.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.khundadze.Balamb_Docs.dtos.DocumentMinimalResponseDto;
import com.khundadze.Balamb_Docs.dtos.DocumentRequestDto;
import com.khundadze.Balamb_Docs.dtos.DocumentResponseDto;
import com.khundadze.Balamb_Docs.exceptions.DocumentNotFoundException;
import com.khundadze.Balamb_Docs.models.Document;
import com.khundadze.Balamb_Docs.repositories.DocumentRepository;

@Service
public class DocumentService {

    DocumentMapper mapper;
    DocumentRepository repo;

    public DocumentService(DocumentMapper mapper, DocumentRepository repo) {
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
}
