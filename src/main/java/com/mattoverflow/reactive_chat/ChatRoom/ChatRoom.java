package com.mattoverflow.reactive_chat.ChatRoom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoom {

    public ChatRoom(String name){
        this.id = UUID.randomUUID().toString();
        this.name = name;
    }

    @Id
    private String id;

    private String name;
}
