package com.mattoverflow.reactive_chat.ChatRoom;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ChatRoomRepository extends ReactiveCrudRepository<ChatRoom, String> {
}
