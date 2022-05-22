package com.grpcflix.aggregator.service;

import com.grpcflix.aggregator.dto.RecommendedMovie;
import com.grpcflix.aggregator.dto.UserGenre;
import com.mike.grpcflix.common.Genre;
import com.mike.grpcflix.movie.MovieSearchRequest;
import com.mike.grpcflix.movie.MovieSearchResponse;
import com.mike.grpcflix.movie.MovieServiceGrpc;
import com.mike.grpcflix.user.UserGenreUpdateRequest;
import com.mike.grpcflix.user.UserResponse;
import com.mike.grpcflix.user.UserSearchRequest;
import com.mike.grpcflix.user.UserServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserMovieService {
    @GrpcClient("user-service")  // shall be the same as declared in application.yaml
    private UserServiceGrpc.UserServiceBlockingStub userStub;

    @GrpcClient("movie-service")
    private MovieServiceGrpc.MovieServiceBlockingStub movieStub;

    public List<RecommendedMovie> getUserMovieSuggestion(String loginId){
        Genre genre = userStub.getUserGenre(UserSearchRequest.newBuilder().setLoginId(loginId).build()).getGenre();

        MovieSearchResponse movieSearchResponse = movieStub.getMovies(MovieSearchRequest.newBuilder().setGenre(genre).build());

        List<RecommendedMovie> movies = movieSearchResponse.getMovieList()
                .stream()
                .map(movieDto -> new RecommendedMovie(
                        movieDto.getTitle(),
                        movieDto.getYear(),
                        movieDto.getRating()
                ))
                .collect(Collectors.toList());

        return movies;

    }

    public void updateUserGenre(UserGenre userGenre){
        UserGenreUpdateRequest userGenreUpdateRequest = UserGenreUpdateRequest.newBuilder()
                .setLoginId(userGenre.getLoginId())
                .setGenre(Genre.valueOf(userGenre.getGenre().toUpperCase()))
                .build();
        UserResponse userResponse = userStub.updateUserGenre(userGenreUpdateRequest);
    }
}
