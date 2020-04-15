package com.mattoverflow.reactive_chat.User;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Component
public class UserController {
    @Bean
    RouterFunction<ServerResponse> userRoutes(UserHandler userHandler) {
        return route()
                .GET("/users", userHandler::all)
                .GET("/users/{id}", userHandler::get)
                .POST("/users", userHandler::create)
                .build();
    }
}
