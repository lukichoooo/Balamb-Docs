package com.khundadze.Balamb_Docs.services;

import org.springframework.stereotype.Service;

import com.khundadze.Balamb_Docs.dtos.DocumentPermissionUserRoleDto;
import com.khundadze.Balamb_Docs.models.DocumentPermission;

@Service
public class DocumentPermissionMapper {

    public DocumentPermissionUserRoleDto toDto(DocumentPermission documentPermission) {
        return new DocumentPermissionUserRoleDto(documentPermission.getUser().getUsername(),
                documentPermission.getRole());
    }
}
