package com.mattoverflow.reactive_chat.UserTests;

import com.mattoverflow.reactive_chat.ChatRoom.ChatRoom;
import com.mattoverflow.reactive_chat.User.*;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Log4j2
@WebFluxTest
@Import({UserController.class, UserService.class, UserHandler.class})
public class UserEndpointTests {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void getAll() {

        log.info("running  " + this.getClass().getName());

        User user1 = new User(UUID.randomUUID().toString(), "A user");
        User user2 = new User(UUID.randomUUID().toString(), "B user");

        Mockito
                .when(this.userRepository.findAll())
                .thenReturn(Flux.just(user1, user2));

        this.webTestClient
                .get()
                .uri("/users")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.[0].id").isEqualTo(user1.getId())
                .jsonPath("$.[0].name").isEqualTo(user1.getName())
                .jsonPath("$.[1].id").isEqualTo(user2.getId())
                .jsonPath("$.[1].name").isEqualTo(user2.getName());
    }

    @Test
    public void save() {
        User postData = new User("Test user save");
        Mockito
                .when(this.userRepository.save(Mockito.any(User.class)))
                .thenReturn(Mono.just(postData));
        this.webTestClient
                .post()
                .uri("/users")
                .body(Mono.just(postData), User.class)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    public void getById() {

        User user = new User(UUID.randomUUID().toString(), "Test get user by ID");

        Mockito
                .when(this.userRepository.findById(user.getId()))
                .thenReturn(Mono.just(user));

        this.webTestClient
                .get()
                .uri("/users/" + user.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(user.getId())
                .jsonPath("$.name").isEqualTo(user.getName());
    }
}
