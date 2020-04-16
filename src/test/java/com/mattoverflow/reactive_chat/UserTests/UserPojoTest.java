package com.mattoverflow.reactive_chat.UserTests;

import com.mattoverflow.reactive_chat.User.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class UserPojoTest {

    @Test
    public void create() {
        //Test empty constructor
        User user = new User();
        Assertions.assertNull(user.getId());
        Assertions.assertNull(user.getName());

        //Test all arguments constructor
        user = new User("2", "Matt");
        Assertions.assertEquals(user.getId(), "2");
        Assertions.assertEquals(user.getName(), "Matt");

        //Test auto assigning id
        user = new User("Matt");
        Assertions.assertNotNull(user.getId());
        Assertions.assertEquals(user.getName(), "Matt");
    }
}
