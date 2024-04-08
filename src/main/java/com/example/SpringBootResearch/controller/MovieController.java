package com.example.SpringBootResearch.controller;

import com.example.SpringBootResearch.dto.MovieDto;
import com.example.SpringBootResearch.service.MovieService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.*;
import javax.swing.plaf.PanelUI;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/movie")
public class MovieController {

    private final MovieService movieService;


    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping("/add-movie")
    public ResponseEntity<MovieDto> addMovieHandler(
            @RequestPart MultipartFile file,
            @RequestPart String movieDto
    ) throws IOException {
        MovieDto dto = convertToMovieDto(movieDto);
        return new ResponseEntity<>(movieService.addMovie(dto, file), HttpStatus.CREATED);
    }


    private MovieDto convertToMovieDto(String movieDtoObj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(movieDtoObj, MovieDto.class);
    }



    @GetMapping("/{movieId}")
    public ResponseEntity<MovieDto> getMovieIdHanlder(@PathVariable Integer movieId) {
        return ResponseEntity.ok(movieService.getMovie(movieId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<MovieDto>> getAllMovie() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }


    @PutMapping("/update/{movieId}")
    public ResponseEntity<MovieDto> updateMovieHandler(
        @PathVariable Integer movieId,
        @RequestPart MultipartFile file,
        @RequestPart String movieDtoObj
    ) throws IOException {
        if (file.isEmpty()) file = null;
        MovieDto movieDto = convertToMovieDto(movieDtoObj);
        return ResponseEntity.ok(movieService.updateMovie(movieId,movieDto,file));
    }

    @DeleteMapping("/delete/{movieId}")
    public ResponseEntity<String> deleteMovieHandler(@PathVariable Integer movieId) throws IOException {
        return ResponseEntity.ok(movieService.deleteMovie(movieId));
    }


}
