package com.khundadze.DocumentTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.khundadze.Balamb_Docs.dtos.DocumentRequestDto;
import com.khundadze.Balamb_Docs.dtos.DocumentResponseDto;
import com.khundadze.Balamb_Docs.exceptions.DocumentNotFoundException;
import com.khundadze.Balamb_Docs.models.Document;
import com.khundadze.Balamb_Docs.repositories.DocumentRepository;
import com.khundadze.Balamb_Docs.services.DocumentMapper;
import com.khundadze.Balamb_Docs.services.DocumentService;

public class DocumentServiceTest {

    @InjectMocks
    DocumentService documentService;

    @Mock
    DocumentMapper documentMapper;

    @Mock
    DocumentRepository documentRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void save() {
        DocumentResponseDto responseDto = new DocumentResponseDto(1L, "name", "description", "content");
        DocumentRequestDto requestDto = new DocumentRequestDto("name", "description", "content");
        Document document = new Document("name", "description", "content");

        when(documentMapper.toDocument(requestDto)).thenReturn(document);
        when(documentRepository.save(document)).thenReturn(document);
        when(documentMapper.toDocumentResponseDto(document)).thenReturn(responseDto);

        DocumentResponseDto result = documentService.save(requestDto);

        assertEquals(responseDto, result);
    }

    @Test
    public void findById() {
        Long id = 1L;
        DocumentResponseDto responseDto = new DocumentResponseDto(1L, "name", "description", "content");
        Document document = new Document("name", "description", "content");

        when(documentRepository.findById(id)).thenReturn(java.util.Optional.of(document));
        when(documentMapper.toDocumentResponseDto(document)).thenReturn(responseDto);

        DocumentResponseDto result = documentService.findById(id);

        assertEquals(responseDto, result);
    }

    @Test
    public void findById_notFound() {
        Long id = 1L;

        when(documentRepository.findById(id)).thenReturn(java.util.Optional.empty());

        DocumentNotFoundException ex = assertThrows(DocumentNotFoundException.class,
                () -> documentService.findById(id));

        assertEquals("Document not found with id: " + id, ex.getMessage());
    }

    // TODO finish tests
}
