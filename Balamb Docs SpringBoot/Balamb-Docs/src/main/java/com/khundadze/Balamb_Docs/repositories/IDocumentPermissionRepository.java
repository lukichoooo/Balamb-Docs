package com.khundadze.Balamb_Docs.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.khundadze.Balamb_Docs.models.DocumentPermission;
import com.khundadze.Balamb_Docs.models.DocumentPermissionId;

public interface IDocumentPermissionRepository extends JpaRepository<DocumentPermission, DocumentPermissionId> {

    List<DocumentPermission> findByDocument_Id(Long documentId);

    List<DocumentPermission> findByUser_Id(Long userId);

    Optional<DocumentPermission> findByDocument_IdAndUser_Id(Long documentId, Long userId);
}
