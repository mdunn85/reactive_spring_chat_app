package com.mattoverflow.reactive_chat.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
public class User {

    public User(String name){
        this.id = UUID.randomUUID().toString();
        this.name = name;
    }

    @Id
    private String id;

    private String name;
}
