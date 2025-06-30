package com.khundadze.DocumentTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.khundadze.Balamb_Docs.dtos.DocumentMinimalResponseDto;
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
    DocumentMapper mapper;

    @Mock
    DocumentRepository repo;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void save() {
        DocumentResponseDto responseDto = new DocumentResponseDto(1L, "name", "description", "content");
        DocumentRequestDto requestDto = new DocumentRequestDto("name", "description", "content");
        Document document = new Document("name", "description", "content");

        when(mapper.toDocument(requestDto)).thenReturn(document);
        when(repo.save(document)).thenReturn(document);
        when(mapper.toDocumentResponseDto(document)).thenReturn(responseDto);

        DocumentResponseDto result = documentService.save(requestDto);

        assertEquals(responseDto, result);
    }

    @Test
    public void findById() {
        Long id = 1L;
        DocumentResponseDto responseDto = new DocumentResponseDto(1L, "name", "description", "content");
        Document document = new Document("name", "description", "content");

        when(repo.findById(id)).thenReturn(java.util.Optional.of(document));
        when(mapper.toDocumentResponseDto(document)).thenReturn(responseDto);

        DocumentResponseDto result = documentService.findById(id);

        assertEquals(responseDto, result);
    }

    @Test
    public void findById_notFound() {
        Long id = 1L;

        when(repo.findById(id)).thenReturn(java.util.Optional.empty());

        DocumentNotFoundException ex = assertThrows(DocumentNotFoundException.class,
                () -> documentService.findById(id));

        assertEquals("Document not found with id: " + id, ex.getMessage());
    }

    @Test
    public void findByNameLike() {
        String name = "name";
        DocumentMinimalResponseDto responseDto = new DocumentMinimalResponseDto(1L, "name");
        Document document = new Document("name", "description", "content");

        when(repo.findTop10ByNameStartsWithIgnoreCase(name)).thenReturn(List.of(document));
        when(mapper.toDocumentMinimalResponseDto(document)).thenReturn(responseDto);

        List<DocumentMinimalResponseDto> result = documentService.findByNameLike(name);

        assertEquals(List.of(responseDto), result);
    }

    @Test
    public void findByNameLike_notFound() {
        String name = "name";

        when(repo.findTop10ByNameStartsWithIgnoreCase(name)).thenReturn(List.of());

        DocumentNotFoundException ex = assertThrows(DocumentNotFoundException.class,
                () -> documentService.findByNameLike(name));

        assertEquals("Document not found with name: " + name, ex.getMessage());
    }

    @Test
    public void getPage() {
        int pageNumber = 1;
        DocumentResponseDto responseDto = new DocumentResponseDto(1L, "name", "description", "content");
        Document document = new Document("name", "description", "content");

        when(repo.findAll(PageRequest.of(pageNumber, 12))).thenReturn(new PageImpl<>(List.of(document)));
        when(mapper.toDocumentResponseDto(document)).thenReturn(responseDto);

        List<DocumentResponseDto> result = documentService.getPage(pageNumber);

        assertEquals(List.of(responseDto), result);
    }

    @Test
    public void updateById() { // TODO
    }
}