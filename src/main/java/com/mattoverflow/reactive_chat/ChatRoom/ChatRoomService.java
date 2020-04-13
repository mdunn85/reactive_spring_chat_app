package com.mattoverflow.reactive_chat.ChatRoom;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Log4j2
@Service
@AllArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public Flux<ChatRoom> all() {
        return this.chatRoomRepository.findAll();
    }

    public Mono<ChatRoom> get(String id) {
        return this.chatRoomRepository.findById(id);
    }

    public Mono<ChatRoom> insert(String name) {
        return this.chatRoomRepository.save(new ChatRoom(UUID.randomUUID().toString(), name));
    }
}
