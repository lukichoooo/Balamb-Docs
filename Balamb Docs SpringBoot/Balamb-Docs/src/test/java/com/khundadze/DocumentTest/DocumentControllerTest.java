package com.khundadze.DocumentTest;

import java.nio.file.AccessDeniedException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.khundadze.Balamb_Docs.controllers.DocumentController;
import com.khundadze.Balamb_Docs.dtos.DocumentMediumResponseDto;
import com.khundadze.Balamb_Docs.dtos.DocumentMinimalResponseDto;
import com.khundadze.Balamb_Docs.dtos.DocumentRequestDto;
import com.khundadze.Balamb_Docs.dtos.DocumentResponseDto;
import com.khundadze.Balamb_Docs.dtos.PageResponse;
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
        DocumentRequestDto requestDto = new DocumentRequestDto("name", "description", "content", false);

        when(service.save(requestDto)).thenReturn(new DocumentResponseDto(1L, "name", "description", "content", false));

        DocumentResponseDto expected = new DocumentResponseDto(1L, "name", "description", "content", false);
        DocumentResponseDto result = controller.save(requestDto);
        assertEquals(expected, result);
    }

    @Test
    public void findById() throws AccessDeniedException {
        Long id = 1L;
        DocumentResponseDto expected = new DocumentResponseDto(1L, "name", "description", "content", false);
        when(service.findById(id)).thenReturn(expected);
        DocumentResponseDto result = controller.findById(id);
        assertEquals(expected, result);
    }

    @Test
    public void findByNameLike() {
        String name = "name";
        DocumentMinimalResponseDto expected = new DocumentMinimalResponseDto(1L, "name", false);
        when(service.findByNameLike(name)).thenReturn(List.of(expected));
        List<DocumentMinimalResponseDto> result = controller.findByNameLike(name);
        assertEquals(List.of(expected), result);
    }

    @Test
    public void getPage() {
        int pageNumber = 1;

        DocumentMediumResponseDto dto = new DocumentMediumResponseDto(1L, "name", "description", false);

        PageResponse<DocumentMediumResponseDto> pageResponse = new PageResponse<>(
                List.of(dto),
                3, // totalPages
                36L, // totalItems
                pageNumber, // currentPage
                12 // pageSize
        );

        when(service.getPage(pageNumber)).thenReturn(pageResponse);

        PageResponse<DocumentMediumResponseDto> result = controller.getPage(pageNumber);

        assertEquals(pageResponse, result);
        assertEquals(1, result.currentPage());
        assertEquals(3, result.totalPages());
        assertEquals(36, result.totalItems());
        assertEquals(List.of(dto), result.items());
    }

}
