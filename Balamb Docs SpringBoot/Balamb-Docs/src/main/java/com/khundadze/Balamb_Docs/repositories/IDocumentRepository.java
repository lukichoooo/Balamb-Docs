package com.khundadze.Balamb_Docs.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.khundadze.Balamb_Docs.models.Document;

import jakarta.transaction.Transactional;

public interface IDocumentRepository extends JpaRepository<Document, Long> {

        Optional<Document> findByName(String name);

        List<Document> findTop10ByNameStartsWithIgnoreCase(String name);

        @Override
        Page<Document> findAll(Pageable pageable);

        @Override
        Optional<Document> findById(Long id);

        @Modifying
        @Transactional
        @Query("UPDATE Document d SET d.content = :content, d.updatedAt = CURRENT_TIMESTAMP WHERE d.id = :id")
        int updateContentById(
                        @Param("id") Long id,
                        @Param("content") String content);

        @Modifying
        @Transactional
        @Query("UPDATE Document d SET d.name = :name, d.updatedAt = CURRENT_TIMESTAMP WHERE d.id = :id")
        int updateNameById(
                        @Param("id") Long id,
                        @Param("name") String name);

        @Modifying
        @Transactional
        @Query("UPDATE Document d SET d.description = :description, d.updatedAt = CURRENT_TIMESTAMP WHERE d.id = :id")
        int updateDescriptionById(
                        @Param("id") Long id,
                        @Param("description") String description);

        Page<Document> findAllByIdIn(List<Long> ids, Pageable pageable);

        // Optional: search by name with paging
        Page<Document> findByNameStartsWithIgnoreCase(String name, Pageable pageable);

        // Optional: search by description with paging
        Page<Document> findByDescriptionStartsWithIgnoreCase(String description, Pageable pageable);

        // Optional: search by content with paging
        Page<Document> findByContentStartsWithIgnoreCase(String content, Pageable pageable);

        // Optional: search by name, description, or content with paging
        Page<Document> findByNameStartsWithIgnoreCaseOrDescriptionStartsWithIgnoreCaseOrContentStartsWithIgnoreCase(
                        String name,
                        String description,
                        String content,
                        Pageable pageable);
}
