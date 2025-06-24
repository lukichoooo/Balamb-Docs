package com.khundadze.Balamb_Docs.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.khundadze.Balamb_Docs.models.Document;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    Optional<Document> findByName(String name);

    List<Document> findTop10ByNameStartsWithIgnoreCase(String name);

    // ✅ Add this:
    Page<Document> findAll(Pageable pageable);

    // Optional: search by name with paging
    Page<Document> findByNameStartsWithIgnoreCase(String name, Pageable pageable);
}
