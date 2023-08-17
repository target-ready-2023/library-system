package com.target.ready.library.system.controller;


import com.target.ready.library.system.entity.Book;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.target.ready.library.system.entity.UserProfile;
import com.target.ready.library.system.exceptions.ResourceNotFoundException;
import com.target.ready.library.system.exceptions.ResourceAlreadyExistsException;
import com.target.ready.library.system.exceptions.ResourceNotFoundException;
import com.target.ready.library.system.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

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
    public void getAllUsersTest() throws ResourceNotFoundException {

        List<UserProfile> userList = new ArrayList<>();
        userList.add(new UserProfile(1, "User1", "Librarian"));
        userList.add(new UserProfile(2, "User2", "Student"));

        when(userService.getAllUsers(0,5)).thenReturn(userList);
        ResponseEntity<?> response = userController.getAllUsers(0);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(userList, response.getBody());
    }

    @Test
    public void fetchAllUsersTest() throws ResourceNotFoundException {

        List<UserProfile> userList = new ArrayList<>();
        userList.add(new UserProfile(1, "User1", "Librarian"));
        userList.add(new UserProfile(2, "User2", "Student"));

        when(userService.fetchAllUsers()).thenReturn(userList);
        ResponseEntity<?> response = userController.fetchAllUsers();

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(userList, response.getBody());
    }

    @Test
    public void getTotalUsersCountTest() {
        List<UserProfile> userList = new ArrayList<>();
        userList.add(new UserProfile(1, "User1", "Librarian"));
        userList.add(new UserProfile(2, "User2", "Student"));

        Mono<Long> serviceResult=Mono.just(0L);
        when(userService.getTotalUsersCount()).thenReturn(serviceResult);
        ResponseEntity<Mono<Long>> categoryResult=userController.getTotalUsersCount();
        Assertions.assertEquals(HttpStatus.OK, categoryResult.getStatusCode());
        Assertions.assertEquals(serviceResult,categoryResult.getBody());
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
