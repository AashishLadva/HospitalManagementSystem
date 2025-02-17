package com.aashish.hospital_management_system.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@AllArgsConstructor
public class PaginatedResponse<T> {
    // The actual data
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private int pageSize;
    private boolean isLast;
    private List<T> content;

    // Convert a Page<T> object into a PaginatedResponse<T>
    public static <T> PaginatedResponse<T> fromPage(Page<T> page) {
        return new PaginatedResponse<>(
                page.getNumber(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.getSize(),
                page.isLast(),
                page.getContent()
        );
    }
}
