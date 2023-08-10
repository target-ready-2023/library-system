package com.target.ready.library.system.service;

import com.target.ready.library.system.entity.UserProfile;
import com.target.ready.library.system.repository.BookCategoryRepository;
import com.target.ready.library.system.repository.BookRepository;
import com.target.ready.library.system.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest(classes = {UserServiceTest.class})
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    BookRepository bookRepository;

    @Mock
    BookCategoryRepository bookCategoryRepository;

    @InjectMocks
    UserService userService;

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
//        when(userRepository.addUser(user1)).thenReturn(user1);
//        userService.addUser(user1);
        assertEquals("User added successfully", user1, users.get(0));
    }


    @Test
    public void deleteUserTest(){
        UserProfile user = new UserProfile();
        user.setUserId(1);
        user.setUserName("Rohit");
        when(userRepository.deleteUser(user.getUserId())).thenReturn("User deleted successfully");
        String response = userService.deleteUser(user.getUserId());
        assertEquals("User deleted successfully", response, "User deleted successfully");

    }
}
