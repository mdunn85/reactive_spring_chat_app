package com.mattoverflow.reactive_chat.ChatRoomTests;

import com.mattoverflow.reactive_chat.ChatRoom.ChatRoom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ChatRoomPojoTest {

    @Test
    public void create() {
        //Test empty constructor
        ChatRoom chatRoom = new ChatRoom();
        Assertions.assertNull(chatRoom.getId());
        Assertions.assertNull(chatRoom.getName());

        //Test all arguments constructor
        chatRoom = new ChatRoom("2", "Matt");
        Assertions.assertEquals(chatRoom.getId(), "2");
        Assertions.assertEquals(chatRoom.getName(), "Matt");

        //Test auto assigning id
        chatRoom = new ChatRoom("Matt");
        Assertions.assertNotNull(chatRoom.getId());
        Assertions.assertEquals(chatRoom.getName(), "Matt");
    }
}
