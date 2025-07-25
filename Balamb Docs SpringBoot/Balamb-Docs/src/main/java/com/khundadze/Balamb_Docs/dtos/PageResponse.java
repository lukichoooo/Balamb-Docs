package com.khundadze.Balamb_Docs.dtos;

import java.util.List;

public record PageResponse<T>(
        List<T> items,
        int totalPages,
        long totalItems,
        int currentPage,
        int pageSize) {
}
