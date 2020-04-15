package com.mattoverflow.reactive_chat.ChatRoomTests;

import com.mattoverflow.reactive_chat.ChatRoom.ChatRoom;
import com.mattoverflow.reactive_chat.ChatRoom.ChatRoomRepository;
import com.mattoverflow.reactive_chat.ChatRoom.ChatRoomService;
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
@Import(ChatRoomService.class)
public class ChatRoomServiceTests {

    private final ChatRoomService service;
    private final ChatRoomRepository repository;

    public ChatRoomServiceTests(@Autowired ChatRoomService service, @Autowired ChatRoomRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @Test
    public void getAll() {

        log.info("running  " + this.getClass().getName());

        Flux<ChatRoom> saved = repository.saveAll(Flux.just(
                new ChatRoom(UUID.randomUUID().toString(), "Test 1"),
                new ChatRoom(UUID.randomUUID().toString(), "Test 2"),
                new ChatRoom(UUID.randomUUID().toString(), "Test 3")));

        Flux<ChatRoom> composite = service.all().thenMany(saved);

        Predicate<ChatRoom> match = chatRoom -> saved.any(saveItem -> saveItem.equals(chatRoom)).block(Duration.ofSeconds(10));

        StepVerifier
                .create(composite)
                .expectNextMatches(match)
                .expectNextMatches(match)
                .expectNextMatches(match)
                .verifyComplete();
    }

    @Test
    public void save() {
        Mono<ChatRoom> chatRoomMono = this.service.insert("Save test");
        StepVerifier
                .create(chatRoomMono)
                .expectNextMatches(saved -> StringUtils.hasText(saved.getId()))
                .verifyComplete();
    }

    @Test
    public void getById() {
        String test = "Get test";
        Mono<ChatRoom> deleted = this.service
                .insert(test)
                .flatMap(saved -> this.service.get(saved.getId()));
        StepVerifier
                .create(deleted)
                .expectNextMatches(chatRoom -> StringUtils.hasText(chatRoom.getId()) && test.equalsIgnoreCase(chatRoom.getName()))
                .verifyComplete();
    }
}
