package com.khundadze.DocumentTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.khundadze.Balamb_Docs.dtos.DocumentMinimalResponseDto;
import com.khundadze.Balamb_Docs.dtos.DocumentRequestDto;
import com.khundadze.Balamb_Docs.dtos.DocumentResponseDto;
import com.khundadze.Balamb_Docs.dtos.DocumentMediumResponseDto;
import com.khundadze.Balamb_Docs.models.Document;
import com.khundadze.Balamb_Docs.services.DocumentMapper;

public class DocumentMapperTest {

    private DocumentMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = new DocumentMapper();
    }

    @Test
    public void toDocumentMinimalResponseDto() {
        Document document = new Document("name", "description", "content");
        DocumentMinimalResponseDto minimalResponseDto = mapper.toDocumentMinimalResponseDto(document);
        assertEquals(document.getId(), minimalResponseDto.id());
        assertEquals(document.getName(), minimalResponseDto.name());
    }

    @Test
    public void toDocumentResponseDto() {
        Document document = new Document("name", "description", "content");
        DocumentResponseDto responseDto = mapper.toDocumentResponseDto(document);
        assertEquals(document.getId(), responseDto.id());
        assertEquals(document.getName(), responseDto.name());
        assertEquals(document.getDescription(), responseDto.description());
        assertEquals(document.getContent(), responseDto.content());
    }

    @Test
    public void toDocumentMediumResponseDto() {
        Document document = new Document("name", "description", "content");
        DocumentMediumResponseDto mediumResponseDto = mapper.toDocumentMediumResponseDto(document);
        assertEquals(document.getId(), mediumResponseDto.id());
        assertEquals(document.getName(), mediumResponseDto.name());
        assertEquals(document.getDescription(), mediumResponseDto.description());
    }

    @Test
    public void toDocument() {
        DocumentRequestDto requestDto = new DocumentRequestDto("name", "description", "content", false);
        Document document = mapper.toDocument(requestDto);
        assertEquals(requestDto.name(), document.getName());
        assertEquals(requestDto.description(), document.getDescription());
        assertEquals(requestDto.content(), document.getContent());
    }
}
