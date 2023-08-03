package com.target.ready.library.system.controller;


import com.target.ready.library.system.entity.UserProfile;
import com.target.ready.library.system.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest(classes = {UserControllerTest.class})
public class UserControllerTest {

    @Mock
    UserService userService;

    @InjectMocks
    UserController userController;

    @Test
    public void addUserTest(){
        List<UserProfile> users = new ArrayList<>();
        UserProfile user1 = new UserProfile();
        user1.setUserId(1);
        user1.setUserName("Rohit");
        users.add(user1);
        UserProfile user2 = new UserProfile();
        user2.setUserId(2);
        user2.setUserName("Kirti");
        users.add(user2);
        when(userService.addUser(user1)).thenReturn(String.valueOf(user1));
        userController.addUser(user1);
        assertEquals("User added successfully", user1, users.get(0));

    }
}
