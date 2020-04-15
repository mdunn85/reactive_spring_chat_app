package com.mattoverflow.reactive_chat.UserTests;

import com.mattoverflow.reactive_chat.ChatRoom.ChatRoom;
import com.mattoverflow.reactive_chat.ChatRoom.ChatRoomRepository;
import com.mattoverflow.reactive_chat.ChatRoom.ChatRoomService;
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

import java.time.Duration;
import java.util.UUID;
import java.util.function.Predicate;

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

        Flux<User> saved = repository.saveAll(Flux.just(
                new User("Test User 1"),
                new User("Test User 2"),
                new User("Test User 3")));

        Flux<User> composite = service.all().thenMany(saved);

        Predicate<User> match = user -> saved.any(saveItem -> saveItem.equals(user)).block(Duration.ofSeconds(10));

        StepVerifier
                .create(composite)
                .expectNextMatches(match)
                .expectNextMatches(match)
                .expectNextMatches(match)
                .verifyComplete();
    }

    @Test
    public void save() {
        Mono<User> userMono = this.service.create("Save user test");
        StepVerifier
                .create(userMono)
                .expectNextMatches(saved -> StringUtils.hasText(saved.getId()))
                .verifyComplete();
    }

    @Test
    public void getById() {
        String test = "Get user test";
        Mono<User> deleted = this.service
                .create(test)
                .flatMap(saved -> this.service.get(saved.getId()));
        StepVerifier
                .create(deleted)
                .expectNextMatches(user -> StringUtils.hasText(user.getId()) && test.equalsIgnoreCase(user.getName()))
                .verifyComplete();
    }
}
