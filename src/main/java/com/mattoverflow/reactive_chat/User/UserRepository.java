package com.mattoverflow.reactive_chat.User;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UserRepository extends ReactiveCrudRepository<User,String> {
}
