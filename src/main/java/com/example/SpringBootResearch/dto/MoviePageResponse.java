package com.example.SpringBootResearch.dto;

import java.util.List;

public record MoviePageResponse (
        List<MovieDto> movieDtos,
        Integer pageNumber,
        Integer pageSize,
        long totalElements,
        int totalPages,
        Boolean isLast
) {
}
