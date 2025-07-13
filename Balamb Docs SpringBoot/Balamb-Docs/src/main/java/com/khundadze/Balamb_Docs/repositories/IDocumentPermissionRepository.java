package com.khundadze.Balamb_Docs.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.khundadze.Balamb_Docs.models.DocumentPermission;
import com.khundadze.Balamb_Docs.models.DocumentPermissionId;
import com.khundadze.Balamb_Docs.models.DocumentRole;

import jakarta.transaction.Transactional;

public interface IDocumentPermissionRepository extends JpaRepository<DocumentPermission, DocumentPermissionId> {

    List<DocumentPermission> findByDocument_Id(Long documentId);

    List<DocumentPermission> findByUser_Id(Long userId);

    Optional<DocumentPermission> findByDocument_IdAndUser_Id(Long documentId, Long userId);

    @Transactional
    void deleteAllByDocumentId(Long documentId);

    Optional<DocumentPermission> findByDocument_IdAndRole(Long documentId, DocumentRole role);

    @Modifying
    @Query("DELETE FROM DocumentPermission dp WHERE dp.document.id = :documentId AND dp.user.username = :username")
    void deleteByDocumentIdAndUsername(@Param("documentId") Long documentId, @Param("username") String username);

}
