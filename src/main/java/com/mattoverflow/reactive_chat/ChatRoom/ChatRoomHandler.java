package com.mattoverflow.reactive_chat.ChatRoom;

import lombok.AllArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@AllArgsConstructor
public class ChatRoomHandler {

    private final ChatRoomService chatRoomService;

    Mono<ServerResponse> get(ServerRequest r) {
        return defaultReadResponse(this.chatRoomService.get(r.pathVariable("id")));
    }

    Mono<ServerResponse> all(ServerRequest r) {
        return defaultReadResponse(this.chatRoomService.all());
    }

    Mono<ServerResponse> create(ServerRequest r) {
        return defaultWriteResponse(r.bodyToMono(ChatRoom.class).flatMap(chatRoom -> this.chatRoomService.insert(chatRoom.getName())));
    }

    private static Mono<ServerResponse> defaultWriteResponse(Publisher<ChatRoom> chatRooms) {
        return Mono
                .from(chatRooms)
                .flatMap(chatRoom -> ServerResponse
                        .created(URI.create("/chat_rooms/" + chatRoom.getId()))
                        .build()
                );
    }

    private static Mono<ServerResponse> defaultReadResponse(Publisher<ChatRoom> chatRooms) {
        return ServerResponse
                .ok()
                .body(chatRooms, ChatRoom.class);
    }
}
