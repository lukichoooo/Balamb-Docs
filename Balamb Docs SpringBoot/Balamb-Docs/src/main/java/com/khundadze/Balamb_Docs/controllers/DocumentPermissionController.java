package com.khundadze.Balamb_Docs.controllers;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.khundadze.Balamb_Docs.dtos.DocumentPermissionUserRoleDto;
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
    public List<DocumentPermissionUserRoleDto> getRolesByDocumentId(
            @PathVariable("documentId") Long documentId) {

        return documentPermissionService.getRolesByDocumentId(documentId);
    }

    @GetMapping("/getRolesByUserId/{userId}")
    public List<DocumentRole> getRolesByUserId(
            @PathVariable("userId") Long userId) {
        return documentPermissionService.getRolesByUserId(userId);
    }

    @GetMapping("/getPermission/{documentId}/{userId}")
    public DocumentPermission getPermission(
            @PathVariable("documentId") Long documentId,
            @PathVariable("userId") Long userId) {

        return documentPermissionService.getPermission(documentId, userId);
    }

    @PostMapping("/createDocumentPermission/{documentId}/{username}/{role}")
    public DocumentPermission createDocumentPermission(
            @PathVariable("documentId") Long documentId,
            @PathVariable("username") String username,
            @PathVariable("role") String role) throws AccessDeniedException {

        return documentPermissionService.createDocumentPermission(documentId, username, role);
    }

    @PostMapping("/updateDocumentPermission/{documentId}/{username}/{role}")
    public DocumentPermission updateDocumentPermission(
            @PathVariable("documentId") Long documentId,
            @PathVariable("username") String username,
            @PathVariable("role") String role) throws AccessDeniedException {

        return documentPermissionService.updateDocumentPermission(documentId, username, role);
    }

    @DeleteMapping("/deleteDocumentPermission/{documentId}/{username}")
    public ResponseEntity<Void> deletePermission(@PathVariable Long documentId, @PathVariable String username)
            throws AccessDeniedException {
        documentPermissionService.deleteByDocumentIdAndUsername(documentId, username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("isCurrentUserAllowedToEditDocument/{documentId}")
    public boolean isCurrentUserAllowedToEditDocument(@PathVariable Long documentId) throws AccessDeniedException {
        return documentPermissionService.isCurrentUserAllowedToEditDocument(documentId);
    }
}
