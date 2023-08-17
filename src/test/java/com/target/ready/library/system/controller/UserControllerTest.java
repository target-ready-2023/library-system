package com.target.ready.library.system.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.target.ready.library.system.entity.UserProfile;
import com.target.ready.library.system.exceptions.ResourceAlreadyExistsException;
import com.target.ready.library.system.exceptions.ResourceNotFoundException;
import com.target.ready.library.system.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest(classes = {UserControllerTest.class})
public class UserControllerTest {

    @Mock
    UserService userService;

    @InjectMocks
    UserController userController;

    @Test
    public void addUserTest() throws JsonProcessingException, ResourceAlreadyExistsException {
        UserProfile userProfile = new UserProfile();
        userProfile.setUserId(1);
        userProfile.setUserName("john");
        userProfile.setUserRole("student");

        when(userService.addUser(userProfile)).thenReturn(userProfile);

        ResponseEntity<?> response = userController.addUser(userProfile);

        //assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("User added successfully",userProfile, response.getBody());

        verify(userService, times(1)).addUser(userProfile);
    }



    @Test
    public void testGetAllUsers() throws ResourceNotFoundException {
        List<UserProfile> users = new ArrayList<>();
        UserProfile user1 = new UserProfile(1, "john", "user");
        UserProfile user2 = new UserProfile(2, "jane", "admin");
        users.add(user1);
        users.add(user2);

        when(userService.getAllUsers()).thenReturn(users);

        ResponseEntity<?> response = userController.getAllUsers();
        //assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());

        assertEquals("", HttpStatus.OK, response.getStatusCode());
        //assertEquals(users.size(), response.getBody().size());

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    public void testAddUserJsonProcessingException() throws JsonProcessingException {
        UserProfile userProfile = new UserProfile(1, "john", "student");

        when(userService.addUser(userProfile)).thenThrow(JsonProcessingException.class);

        assertThrows(RuntimeException.class, () -> {
            userController.addUser(userProfile);
        });

        verify(userService, times(1)).addUser(userProfile);
    }

    @Test
    public void findBookNameByUserIdTest() throws ResourceNotFoundException {
        int userId = 1;

        List<Integer> bookIds = List.of(101, 102);
        List<String> bookNames = List.of("Book A", "Book B");

        when(userService.findBookNameByUserId(userId)).thenReturn(bookNames);

        ResponseEntity<?> response = userController.findBookNameByUserId(userId);

        assertEquals("",HttpStatus.OK, response.getStatusCode());
        assertEquals("",bookNames, response.getBody());

        verify(userService, times(1)).findBookNameByUserId(userId);
    }

    @Test
    public void deleteUserTest(){
        UserProfile user = new UserProfile();
        user.setUserId(1);
        user.setUserName("Rohit");
        when(userService.deleteUser(user.getUserId())).thenReturn("User deleted successfully!!");
        ResponseEntity<String> response = userController.deleteUser(user.getUserId());
        assertEquals("User deleted successfully!!", response.getBody(), "User deleted successfully!!");

    }

    @Test
    public void deleteUserWithBooksCheckedOutTest() {
        int userId = 1;
        String errorMessage = "User has books checked out. Cannot delete user";

        when(userService.deleteUser(userId)).thenReturn(errorMessage);

        ResponseEntity<String> response = userController.deleteUser(userId);

        assertEquals("",HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("","user cannot be deleted, as user has issued a book", response.getBody());

        verify(userService, times(1)).deleteUser(userId);
    }
}
