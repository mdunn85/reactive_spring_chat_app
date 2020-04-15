package com.mattoverflow.reactive_chat.User;

import com.mattoverflow.reactive_chat.ChatRoom.ChatRoom;
import lombok.AllArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@AllArgsConstructor
public class UserHandler {
    private final UserService userService;

    Mono<ServerResponse> get(ServerRequest r) {
        return defaultReadResponse(this.userService.get(r.pathVariable("id")));
    }

    Mono<ServerResponse> all(ServerRequest r) {
        return defaultReadResponse(this.userService.all());
    }

    Mono<ServerResponse> create(ServerRequest r) {
        return defaultWriteResponse(r.bodyToMono(ChatRoom.class).flatMap(chatRoom -> this.userService.create(chatRoom.getName())));
    }

    private static Mono<ServerResponse> defaultWriteResponse(Publisher<User> users) {
        return Mono
                .from(users)
                .flatMap(user -> ServerResponse
                        .created(URI.create("/users/" + user.getId()))
                        .build()
                );
    }

    private static Mono<ServerResponse> defaultReadResponse(Publisher<User> users) {
        return ServerResponse
                .ok()
                .body(users, User.class);
    }
}
