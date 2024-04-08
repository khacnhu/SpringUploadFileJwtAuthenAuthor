package com.example.SpringBootResearch.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer movieId;


    @Column(nullable = false, length = 200)
    @NotBlank(message = "PlEASE PROVIDE MOVIE TITLE")
    private String title;

    @Column(nullable = false)
    @NotBlank(message = "PLEASE PROVIDE MOVIE'S DIRECTOR")
    private String director;


    @Column(nullable = false)
    @NotBlank(message = "PLEASE PROVIDE MOVIE'S STUDIO")
    private String studio;

    @ElementCollection
    @CollectionTable(name = "movie_cast")
    private Set<String> movieCast;


    @Column(nullable = false)
    @NotBlank(message = "PLEASE PROVIDE MOVIE'S RELEASE YEAR")
    private String releaseYear;


    @Column(nullable = false)
    @NotBlank(message = "PLEASE PROVIDE MOVIE'S POSTER")
    private String poster;



}
