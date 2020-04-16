package com.mattoverflow.reactive_chat.UserTests;

import com.mattoverflow.reactive_chat.User.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DataMongoTest
public class UserEntityTest {

    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @Test
    public void persist() {

        User user = new User("Matt");
        Mono<User> savedUser = this.reactiveMongoTemplate.save(user);

        StepVerifier
                .create(savedUser)
                .expectNext(user)
                .verifyComplete();
    }

}
