package com.grpcflix.user.service;

import com.grpcflix.user.entity.User;
import com.grpcflix.user.repository.UserRepository;
import com.mike.grpcflix.common.Genre;
import com.mike.grpcflix.user.UserGenreUpdateRequest;
import com.mike.grpcflix.user.UserResponse;
import com.mike.grpcflix.user.UserSearchRequest;
import com.mike.grpcflix.user.UserServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.Locale;

@GrpcService
public class UserService extends UserServiceGrpc.UserServiceImplBase {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void getUserGenre(UserSearchRequest request, StreamObserver<UserResponse> responseObserver) {
        UserResponse.Builder builder = UserResponse.newBuilder();

        userRepository.findById(request.getLoginId())
                        .ifPresent(user -> builder.setName(user.getName())
                                .setLoginId(user.getLogin())
                                .setGenre(Genre.valueOf(user.getGenre().toUpperCase())));

        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    @Transactional
    public void updateUserGenre(UserGenreUpdateRequest request, StreamObserver<UserResponse> responseObserver) {
        UserResponse.Builder builder = UserResponse.newBuilder();

        this.userRepository.findById(request.getLoginId())
                .ifPresent(user -> {
                    user.setGenre(request.getGenre().toString());
//                    this.userRepository.save(user);

                    builder.setLoginId(user.getLogin())
                            .setName(user.getName())
                            .setGenre(Genre.valueOf(user.getGenre().toUpperCase()));
                });

        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }
}
