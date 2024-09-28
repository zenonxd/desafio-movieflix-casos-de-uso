package com.devsuperior.movieflix.services;

import com.devsuperior.movieflix.dto.MovieCardDTO;
import com.devsuperior.movieflix.dto.MovieDetailsDTO;
import com.devsuperior.movieflix.dto.ReviewDTO;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.entities.Review;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.repositories.ReviewRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    @Autowired
    public MovieRepository movieRepository;

    @Autowired
    public ReviewRepository reviewRepository;

    @Transactional
    public MovieDetailsDTO findById(Long id) {
        if (!movieRepository.findById(id).isPresent()) {
            throw new ResourceNotFoundException("Movie not found");
        } else {
            Movie movie = movieRepository.getReferenceById(id);
            return new MovieDetailsDTO(movie);
        }
    }

    @Transactional
    public Page<MovieDetailsDTO> findAll(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        Page<Movie> movies = movieRepository.findAll(pageable);

        return movies.map(MovieDetailsDTO::new);
    }

    @Transactional
    public Page<MovieCardDTO> findAllByGenre(Long genreId, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        Page<Movie> movies = movieRepository.findByGenreId(genreId, pageable);

        return movies.map(MovieCardDTO::new);
    }

    @Transactional
    public List<ReviewDTO> findReviewsById(Long id) {
        Optional<Review> reviews = reviewRepository.findById(id);

        return reviews.stream().map(ReviewDTO::new).toList();
    }
}
