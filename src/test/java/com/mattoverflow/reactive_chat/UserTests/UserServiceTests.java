package com.mattoverflow.reactive_chat.UserTests;

import com.mattoverflow.reactive_chat.User.User;
import com.mattoverflow.reactive_chat.User.UserRepository;
import com.mattoverflow.reactive_chat.User.UserService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

@Log4j2
@DataMongoTest
@Import(UserService.class)
public class UserServiceTests {

    private final UserService service;
    private final UserRepository repository;

    public UserServiceTests(@Autowired UserService service, @Autowired UserRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @Test
    public void getAll() {

        log.info("running  " + this.getClass().getName());

        List<User> usersList = List.of(
                new User("Test User 1"),
                new User("Test User 2"),
                new User("Test User 3")
        );

        Flux<User> savedUsers = this.repository.saveAll(usersList);

        StepVerifier.create(this.repository.deleteAll()).verifyComplete();

        StepVerifier.create(savedUsers).expectNextCount(3).verifyComplete();

        Flux<User> allUsersFromService = service.all();

        StepVerifier.create(allUsersFromService)
                .expectNextMatches(usersList::contains)
                .expectNextMatches(usersList::contains)
                .expectNextMatches(usersList::contains)
                .verifyComplete();
    }

    @Test
    public void save() {
        Mono<User> userMono = this.service.create("Save user test");

        StepVerifier.create(this.repository.deleteAll()).verifyComplete();

        StepVerifier
                .create(userMono)
                .expectNextMatches(saved -> StringUtils.hasText(saved.getId()))
                .verifyComplete();
    }

    @Test
    public void getById() {
        User user = new User("Test user");

        Mono<User> savedUser = this.repository.save(user);

        StepVerifier.create(this.repository.deleteAll()).verifyComplete();

        StepVerifier
                .create(savedUser)
                .expectNext(user)
                .verifyComplete();

        Mono<User> serviceUser = this.service.get(user.getId());

        StepVerifier.create(serviceUser).expectNext(user).verifyComplete();
    }
}
