package com.khundadze.DocumentTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.khundadze.Balamb_Docs.controllers.DocumentController;
import com.khundadze.Balamb_Docs.dtos.DocumentMinimalResponseDto;
import com.khundadze.Balamb_Docs.dtos.DocumentRequestDto;
import com.khundadze.Balamb_Docs.dtos.DocumentResponseDto;
import com.khundadze.Balamb_Docs.services.DocumentService;

public class DocumentControllerTest {

    @InjectMocks
    private DocumentController controller;

    @Mock
    private DocumentService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void save() {
        DocumentRequestDto requestDto = new DocumentRequestDto("name", "description", "content");

        when(service.save(requestDto)).thenReturn(new DocumentResponseDto(1L, "name", "description", "content"));

        DocumentResponseDto expected = new DocumentResponseDto(1L, "name", "description", "content");
        DocumentResponseDto result = controller.save(requestDto);
        assertEquals(expected, result);
    }

    @Test
    public void findById() {
        Long id = 1L;
        DocumentResponseDto expected = new DocumentResponseDto(1L, "name", "description", "content");
        when(service.findById(id)).thenReturn(expected);
        DocumentResponseDto result = controller.findById(id);
        assertEquals(expected, result);
    }

    @Test
    public void findByNameLike() {
        String name = "name";
        DocumentMinimalResponseDto expected = new DocumentMinimalResponseDto(1L, "name");
        when(service.findByNameLike(name)).thenReturn(List.of(expected));
        List<DocumentMinimalResponseDto> result = controller.findByNameLike(name);
        assertEquals(List.of(expected), result);
    }

    @Test
    public void getPage() {
        int pageNumber = 1;
        DocumentResponseDto expected = new DocumentResponseDto(1L, "name", "description", "content");
        when(service.getPage(pageNumber)).thenReturn(List.of(expected));
        List<DocumentResponseDto> result = controller.getPage(pageNumber);
        assertEquals(List.of(expected), result);
    }
}
