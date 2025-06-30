package com.khundadze.Balamb_Docs.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.khundadze.Balamb_Docs.dtos.DocumentMinimalResponseDto;
import com.khundadze.Balamb_Docs.dtos.DocumentRequestDto;
import com.khundadze.Balamb_Docs.dtos.DocumentResponseDto;
import com.khundadze.Balamb_Docs.services.DocumentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/save")
    public DocumentResponseDto save(@Valid @RequestBody DocumentRequestDto requestDocument) {
        return documentService.save(requestDocument);
    }

    @GetMapping("/findById/{id}")
    public DocumentResponseDto findById(@PathVariable("id") Long id) {
        return documentService.findById(id);
    }

    @GetMapping("/findByName/{name}")
    public DocumentResponseDto findByName(@PathVariable("name") String name) {
        return documentService.findByName(name);
    }

    @GetMapping("/findByNameLike/{name}")
    public List<DocumentMinimalResponseDto> findByNameLike(@PathVariable("name") String name) {
        return documentService.findByNameLike(name);
    }

    @GetMapping("/getPage/{pageNumber}")
    public List<DocumentResponseDto> getPage(@PathVariable("pageNumber") int pageNumber) {
        return documentService.getPage(pageNumber);
    }

    @DeleteMapping("/deleteById/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        documentService.deleteById(id);
    }

    @PutMapping("/updateContentById/{id}")
    public DocumentResponseDto updateContentById(@PathVariable("id") Long id,
            @RequestBody String content) {
        return documentService.updateContentById(id, content);
    }
}
