package com.example.SpringBootResearch.service;

import com.example.SpringBootResearch.dto.MovieDto;
import com.example.SpringBootResearch.dto.MoviePageResponse;
import com.example.SpringBootResearch.entity.Movie;
import com.example.SpringBootResearch.exceptions.FileExistException;
import com.example.SpringBootResearch.exceptions.MovieNotFoundException;
import com.example.SpringBootResearch.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.data.domain.Pageable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final FileService fileService;

    public MovieServiceImpl(MovieRepository movieRepository, FileService fileService) {
        this.movieRepository = movieRepository;
        this.fileService = fileService;
    }

    @Value("${project.poster}")
    private String path;

    @Value("${base.url}")
    private String baseUrl;

    @Override
    public MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException {

        if (Files.exists(Paths.get(path + File.separator + file.getOriginalFilename()))){
            throw new FileExistException("File already exist! Please change file ");
        }


        // 1. upload the file
        String uploadFileName = fileService.uploadFile(path, file);

        // 2. set the value of field 'poster' as filename
        movieDto.setPoster(uploadFileName);

        // 3. map dto to movie object
        Movie movie = new Movie(
                movieDto.getMovieId(),
                movieDto.getTitle(),
                movieDto.getDirector(),
                movieDto.getStudio(),
                movieDto.getMovieCast(),
                movieDto.getReleaseYear(),
                movieDto.getPoster()
        );


        // 4.save the movie object -> save Movie object
        Movie savedMovie = movieRepository.save(movie);


        // 5. generate the postUrl
        String postedUrl = baseUrl + "/file/" + uploadFileName;


        // 6. map Movie object to DTO object and return it
        MovieDto reponse = new MovieDto(
                savedMovie.getMovieId(),
                savedMovie.getTitle(),
                savedMovie.getDirector(),
                savedMovie.getStudio(),
                savedMovie.getMovieCast(),
                savedMovie.getReleaseYear(),
                savedMovie.getPoster(),
                postedUrl
        );

        return reponse;
    }

    @Override
    public MovieDto getMovie(Integer movieId) {

        // 1. check the data in DB and if existed, fetch the data of given ID
        Movie movie = movieRepository.findById(movieId).orElseThrow(
                () -> new MovieNotFoundException("MOVIE ID IS NOT FOUND BY ID = " + movieId)
        );


        // 2. generate posterUrl
        String posterUrl = baseUrl + "/file/" + movie.getPoster();


        // 3. map to MovieDto object and return it
        MovieDto reponse = new MovieDto(
                movie.getMovieId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getStudio(),
                movie.getMovieCast(),
                movie.getReleaseYear(),
                movie.getPoster(),
                posterUrl
        );

        return reponse;
    }

    @Override
    public List<MovieDto> getAllMovies() {

        // 1. fetch all database from DB
        List<Movie> movies = movieRepository.findAll();
        System.out.println("check movies all " + movies);


        List<MovieDto> movieDtos = new ArrayList<>();
        System.out.println("check + moviedtos " + movieDtos);

        // 2. iterator through the list, generate posterUrl for each movie Obj and map to MovieDto Obj
        for (Movie movie: movies) {
            String posterUrl = baseUrl + "/file/" + movie.getPoster();

            MovieDto reponse = new MovieDto(
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getMovieCast(),
                    movie.getReleaseYear(),
                    movie.getPoster(),
                    posterUrl
            );
            System.out.println("check res " + reponse);
            movieDtos.add(reponse);
        }
        System.out.println("check list moviedto " + movieDtos);
        return movieDtos;
    }

    @Override
    public MovieDto updateMovie(Integer movieId, MovieDto movieDto, MultipartFile file) throws IOException {

        // 1 check if movie object exists with given movieId
        Movie movie = movieRepository.findById(movieId).orElseThrow(()-> new MovieNotFoundException("Movie id is not existed"));


        // 2 if file is null, do nothing
        //   if file is not null, then delete existing file associated with the record
        //   and upload the new file
        String fileName = movie.getPoster();
        if( file != null) {
            Files.deleteIfExists(Paths.get(path + File.separator + file.getOriginalFilename()));
            fileName = fileService.uploadFile(path, file);
        }

        // 3. set movieDto's poster value, according to step2
        movieDto.setPoster(fileName);

        // 4. map it to Movie object
        Movie movieNew = new Movie(
                movie.getMovieId(),
                movieDto.getTitle(),
                movieDto.getDirector(),
                movieDto.getStudio(),
                movieDto.getMovieCast(),
                movieDto.getReleaseYear(),
                movieDto.getPoster()
        );

        // 5. save the movie object -> return save movie object
        Movie updateMovie = movieRepository.save(movieNew);

        // 6. generate posterUrl for it
        String posterUrl = baseUrl + "/file/" + fileName;

        // 7. map to MovieDto and return it
        MovieDto movieDtoUpdate = new MovieDto(
                updateMovie.getMovieId(),
                updateMovie.getTitle(),
                updateMovie.getDirector(),
                updateMovie.getStudio(),
                updateMovie.getMovieCast(),
                updateMovie.getReleaseYear(),
                updateMovie.getPoster(),
                posterUrl
        );


        return movieDtoUpdate;
    }

    @Override
    public String deleteMovie(Integer movieId) throws IOException {
        // 1 Check if movie object is existed in database or not
        Movie movie = movieRepository.findById(movieId).orElseThrow(()->new MovieNotFoundException("Movie not found"));


        // 2 delete the file associated with this object
        Files.deleteIfExists(Paths.get(path + File.separator + movie.getPoster()));

        // 3 delete the movie object
        movieRepository.delete(movie);

        return "Movie delete with id = " + movieId;
    }

    @Override
    public MoviePageResponse getAllMoviesWithPagination(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<Movie> moviePage = movieRepository.findAll(pageable);

        List<Movie> movies = moviePage.getContent();

        List<MovieDto> movieDtos = new ArrayList<>();

        for (Movie movie: movies) {
            String posterUrl = baseUrl + "/file/" + movie.getPoster();
            MovieDto movieDto = new MovieDto(
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getMovieCast(),
                    movie.getReleaseYear(),
                    movie.getPoster(),
                    posterUrl
            );
            movieDtos.add(movieDto);

        }

        return new MoviePageResponse(movieDtos, pageNumber, pageSize,
                moviePage.getTotalElements(), moviePage.getTotalPages(),
                moviePage.isLast()
        );
    }

    @Override
    public MoviePageResponse getAllMoviesWithPaginationAndSorting(
            Integer pageNumber, Integer pageSize, String sortBy, String dir) {

        Sort sort = dir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Movie> moviePage = movieRepository.findAll(pageable);

        List<Movie> movies = moviePage.getContent();

        List<MovieDto> movieDtos = new ArrayList<>();

        for (Movie movie: movies) {
            String posterUrl = baseUrl + "/file/" + movie.getPoster();
            MovieDto movieDto = new MovieDto(
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getMovieCast(),
                    movie.getReleaseYear(),
                    movie.getPoster(),
                    posterUrl
            );
            movieDtos.add(movieDto);

        }

        return new MoviePageResponse(movieDtos, pageNumber, pageSize,
                moviePage.getTotalElements(), moviePage.getTotalPages(),
                moviePage.isLast()
        );
    }
}
