package com.mattoverflow.reactive_chat.ChatRoom;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Component
public class ChatRoomController {

    @Bean
    RouterFunction<ServerResponse> chatRoomRoutes(ChatRoomHandler chatRoomHandler) {
        return route()
                .GET("/chat_rooms", chatRoomHandler::all)
                .GET("/chat_rooms/{id}", chatRoomHandler::get)
                .POST("/chat_rooms", chatRoomHandler::create)
                .build();
    }
}
