package com.mattoverflow.reactive_chat.ChatRoomTests;

import com.mattoverflow.reactive_chat.ChatRoom.ChatRoom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DataMongoTest
public class ChatRoomEntityTest {

    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @Test
    public void persist(){
        ChatRoom chatRoom = new ChatRoom("Test chat room");
        Mono<ChatRoom> chatRoomMono = this.reactiveMongoTemplate.save(chatRoom);

        StepVerifier
                .create(chatRoomMono)
                .expectNext(chatRoom)
                .verifyComplete();
    }
}
