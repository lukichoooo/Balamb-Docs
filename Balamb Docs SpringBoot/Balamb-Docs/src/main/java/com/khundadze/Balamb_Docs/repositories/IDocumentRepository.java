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

    Page<Document> findAll(Pageable pageable);

    @Modifying
    @Transactional
    @Query("DELETE FROM Document d WHERE d.id = :id")
    void deleteById(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Document d SET d.content = :content WHERE d.id = :id")
    int updateContentById(
            @Param("id") Long id,
            @Param("content") String content);

    // Optional: search by name with paging
    Page<Document> findByNameStartsWithIgnoreCase(String name, Pageable pageable);

    // Optional: search by description with paging
    Page<Document> findByDescriptionStartsWithIgnoreCase(String description, Pageable pageable);

    // Optional: search by content with paging
    Page<Document> findByContentStartsWithIgnoreCase(String content, Pageable pageable);
}
