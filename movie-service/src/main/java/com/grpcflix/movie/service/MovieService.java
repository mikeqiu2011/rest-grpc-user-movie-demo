package com.grpcflix.movie.service;

import com.grpcflix.movie.repository.MovieRepository;
import com.mike.grpcflix.movie.MovieDto;
import com.mike.grpcflix.movie.MovieSearchRequest;
import com.mike.grpcflix.movie.MovieSearchResponse;
import com.mike.grpcflix.movie.MovieServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class MovieService extends MovieServiceGrpc.MovieServiceImplBase {
    @Autowired
    private MovieRepository repository;

    @Override
    public void getMovies(MovieSearchRequest request, StreamObserver<MovieSearchResponse> responseObserver) {
        MovieSearchResponse.Builder builder = MovieSearchResponse.newBuilder();
        this.repository.getMovieByGenreOrderByYearDesc(request.getGenre().toString())
                .stream()
                .forEach(movie -> {
                    builder.addMovie(
                            MovieDto.newBuilder()
                                    .setRating(movie.getRating())
                                    .setTitle(movie.getTitle())
                                    .setYear(movie.getYear())
                                    .build()
                    );
                });

        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }
}
