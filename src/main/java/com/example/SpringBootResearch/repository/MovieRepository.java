package com.example.SpringBootResearch.repository;

import com.example.SpringBootResearch.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MovieRepository extends JpaRepository<Movie, Integer> {


}
