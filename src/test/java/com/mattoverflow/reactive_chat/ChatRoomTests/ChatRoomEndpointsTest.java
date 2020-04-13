package com.mattoverflow.reactive_chat.ChatRoomTests;

import com.mattoverflow.reactive_chat.ChatRoom.*;
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
@Import({ChatRoomController.class,
        ChatRoomHandler.class, ChatRoomService.class})
public class ChatRoomEndpointsTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ChatRoomRepository repository;

    @Test
    public void getAll() {

        log.info("running  " + this.getClass().getName());

        ChatRoom chatRoom1 = new ChatRoom(UUID.randomUUID().toString(), "A");
        ChatRoom chatRoom2 = new ChatRoom(UUID.randomUUID().toString(), "B");

        Mockito
                .when(this.repository.findAll())
                .thenReturn(Flux.just(chatRoom1, chatRoom2));

        this.webTestClient
                .get()
                .uri("/chat_rooms")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.[0].id").isEqualTo(chatRoom1.getId())
                .jsonPath("$.[0].name").isEqualTo(chatRoom1.getName())
                .jsonPath("$.[1].id").isEqualTo(chatRoom2.getId())
                .jsonPath("$.[1].name").isEqualTo(chatRoom2.getName());
    }

    @Test
    public void save() {
        ChatRoom data = new ChatRoom(UUID.randomUUID().toString(), "Test save 1");
        Mockito
                .when(this.repository.save(Mockito.any(ChatRoom.class)))
                .thenReturn(Mono.just(data));
        this.webTestClient
                .post()
                .uri("/chat_rooms")
                .body(Mono.just(data), ChatRoom.class)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    public void getById() {

        ChatRoom data = new ChatRoom(UUID.randomUUID().toString(), "Test get by ID");

        Mockito
                .when(this.repository.findById(data.getId()))
                .thenReturn(Mono.just(data));

        this.webTestClient
                .get()
                .uri("/chat_rooms/" + data.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(data.getId())
                .jsonPath("$.name").isEqualTo(data.getName());
    }

}
