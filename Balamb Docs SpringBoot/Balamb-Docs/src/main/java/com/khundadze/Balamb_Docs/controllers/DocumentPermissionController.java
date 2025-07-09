package com.khundadze.Balamb_Docs.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.khundadze.Balamb_Docs.models.DocumentPermission;
import com.khundadze.Balamb_Docs.models.DocumentRole;
import com.khundadze.Balamb_Docs.services.DocumentPermissionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/documentPermissions")
@RequiredArgsConstructor
public class DocumentPermissionController {

    private final DocumentPermissionService documentPermissionService;

    @GetMapping("/getRolesByDocumentId/{documentId}")
    public List<DocumentRole> getRolesByDocumentId(@PathVariable("documentId") Long documentId) {
        return documentPermissionService.getRolesByDocumentId(documentId);
    }

    @GetMapping("/getRolesByUserId/{userId}")
    public List<DocumentRole> getRolesByUserId(@PathVariable("userId") Long userId) {
        return documentPermissionService.getRolesByUserId(userId);
    }

    @GetMapping("/getPermission/{documentId}/{userId}")
    public DocumentPermission getPermission(@PathVariable("documentId") Long documentId,
            @PathVariable("userId") Long userId) {

        return documentPermissionService.getPermission(documentId, userId);
    }

    @PostMapping("/createDocumentPermission/{documentId}/{userId}/{role}")
    public DocumentPermission createDocumentPermission(@PathVariable("documentId") Long documentId,
            @PathVariable("userId") Long userId, @PathVariable("role") DocumentRole role) {

        return documentPermissionService.createDocumentPermission(documentId, userId, role);
    }

    @PostMapping("/updateDocumentPermission/{documentId}/{userId}/{role}")
    public DocumentPermission updateDocumentPermission(@PathVariable("documentId") Long documentId,
            @PathVariable("userId") Long userId, @PathVariable("role") DocumentRole role) {

        return documentPermissionService.updateDocumentPermission(documentId, userId, role);
    }
}
