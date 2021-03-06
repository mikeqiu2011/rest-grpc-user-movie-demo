package com.grpcflix.movie.service;

import com.grpcflix.movie.repository.MovieRepository;
import com.mike.grpcflix.movie.MovieDto;
import com.mike.grpcflix.movie.MovieSearchRequest;
import com.mike.grpcflix.movie.MovieSearchResponse;
import com.mike.grpcflix.movie.MovieServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService
public class MovieService extends MovieServiceGrpc.MovieServiceImplBase {
    @Autowired
    private MovieRepository repository;

    @Override
    public void getMovies(MovieSearchRequest request, StreamObserver<MovieSearchResponse> responseObserver) {

        List<MovieDto> moviesDtoList = this.repository.getMovieByGenreOrderByReleaseYearDesc(request.getGenre().toString())
                .stream()
                .map(movie -> MovieDto.newBuilder()
                        .setTitle(movie.getTitle())
                        .setYear(movie.getReleaseYear())
                        .setRating(movie.getRating())
                        .build())
                .collect(Collectors.toList());

        MovieSearchResponse movieSearchResponse = MovieSearchResponse.newBuilder().addAllMovie(moviesDtoList).build();
        responseObserver.onNext(movieSearchResponse);

        responseObserver.onCompleted();
    }
}
