package com.devsuperior.movieflix.services;

import com.devsuperior.movieflix.dto.ReviewDTO;
import com.devsuperior.movieflix.dto.UserDTO;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.entities.Review;
import com.devsuperior.movieflix.entities.User;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.repositories.ReviewRepository;
import com.devsuperior.movieflix.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public ReviewDTO insert(ReviewDTO reviewDTO) {

        if (reviewDTO.getText() == null || reviewDTO.getText().isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Review cannot be empty.");
        }
        Review review = new Review();
        Movie movie = movieRepository.getReferenceById(reviewDTO.getMovieId());
        UserDTO userDTO = userService.getProfile();

        review.setText(reviewDTO.getText());
        review.setMovie(movie);

        review = reviewRepository.save(review);

        reviewDTO = new ReviewDTO();
        reviewDTO.setId(review.getId());
        reviewDTO.setText(review.getText());
        reviewDTO.setMovieId(review.getMovie().getId());
        reviewDTO.setUserId(userDTO.getId());
        reviewDTO.setUserName(userDTO.getName());
        reviewDTO.setUserEmail(userDTO.getEmail());

        return reviewDTO;

    }
}
