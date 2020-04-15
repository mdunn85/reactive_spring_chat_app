package com.mattoverflow.reactive_chat.User;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Flux<User> all() {
        return this.userRepository.findAll();
    }

    public Mono<User> get(String id) {
        return this.userRepository.findById(id);
    }

    public Mono<User> create(String name) {
        return this.userRepository.save(new User(name));
    }

}
