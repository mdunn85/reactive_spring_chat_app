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
import java.util.List;
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

        List<ChatRoom> chatRoomsList = List.of(
                new ChatRoom(UUID.randomUUID().toString(), "Test 1"),
                new ChatRoom(UUID.randomUUID().toString(), "Test 2"),
                new ChatRoom(UUID.randomUUID().toString(), "Test 3")
        );

        Flux<ChatRoom> savedChatRooms = repository.saveAll(chatRoomsList);

        StepVerifier
                .create(this.repository.deleteAll())
                .verifyComplete();

        StepVerifier
                .create(savedChatRooms)
                .expectNextCount(3)
                .verifyComplete();

        Flux<ChatRoom> serviceChatRooms = service.all();

        StepVerifier
                .create(serviceChatRooms)
                .expectNextMatches(chatRoomsList::contains)
                .expectNextMatches(chatRoomsList::contains)
                .expectNextMatches(chatRoomsList::contains)
                .verifyComplete();
    }

    @Test
    public void save() {
        Mono<ChatRoom> chatRoomMono = this.service.insert("Save test");

        StepVerifier.create(this.repository.deleteAll()).verifyComplete();

        StepVerifier
                .create(chatRoomMono)
                .expectNextMatches(saved -> StringUtils.hasText(saved.getId()))
                .verifyComplete();
    }

    @Test
    public void getById() {

        ChatRoom chatRoom = new ChatRoom("Test chat room");

        Mono<ChatRoom> savedChatRoom = this.repository.save(chatRoom);

        StepVerifier.create(this.repository.deleteAll()).verifyComplete();

        StepVerifier.create(savedChatRoom).expectNext(chatRoom).verifyComplete();

        Mono<ChatRoom> serviceChatRoom = this.service.get(chatRoom.getId());

        StepVerifier.create(serviceChatRoom).expectNext(chatRoom).verifyComplete();
    }
}
