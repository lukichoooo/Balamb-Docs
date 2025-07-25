package com.khundadze.Balamb_Docs.services;

import org.springframework.stereotype.Service;

import com.khundadze.Balamb_Docs.dtos.DocumentFullInfoResponseDto;
import com.khundadze.Balamb_Docs.dtos.DocumentMediumResponseDto;
import com.khundadze.Balamb_Docs.dtos.DocumentMinimalResponseDto;
import com.khundadze.Balamb_Docs.dtos.DocumentRequestDto;
import com.khundadze.Balamb_Docs.dtos.DocumentResponseDto;
import com.khundadze.Balamb_Docs.models.Document;

@Service
public class DocumentMapper {

    public DocumentMinimalResponseDto toDocumentMinimalResponseDto(Document document) {
        return new DocumentMinimalResponseDto(document.getId(), document.getName(), document.isPublic());
    }

    public DocumentMediumResponseDto toDocumentMediumResponseDto(Document document) {
        return new DocumentMediumResponseDto(document.getId(),
                document.getName(),
                document.getDescription(),
                document.isPublic());
    }

    public DocumentResponseDto toDocumentResponseDto(Document document) {
        return new DocumentResponseDto(document.getId(),
                document.getName(),
                document.getDescription(),
                document.getContent(),
                document.isPublic());
    }

    public Document toDocument(DocumentRequestDto requestDto) {
        Document document = new Document();
        document.setName(requestDto.name());
        document.setDescription(requestDto.description());
        document.setContent(requestDto.content());
        document.setPublic(requestDto.isPublic());
        return document;
    }

    public DocumentFullInfoResponseDto toDocumentFullInfoResponseDto(Document document) {
        return new DocumentFullInfoResponseDto(document.getId(),
                document.getName(),
                document.isPublic(),
                document.getCreatedAt(),
                document.getUpdatedAt());
    }
}
