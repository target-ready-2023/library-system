package com.target.ready.library.system.service;

import com.target.ready.library.system.entity.Book;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.target.ready.library.system.entity.Book;
import com.target.ready.library.system.entity.UserCatalog;
import com.target.ready.library.system.entity.UserProfile;
import com.target.ready.library.system.repository.BookCategoryRepository;
import com.target.ready.library.system.repository.BookRepository;
import com.target.ready.library.system.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
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
    public void deleteUserTest(){
        UserProfile user = new UserProfile();
        user.setUserId(1);
        user.setUserName("Rohit");
        when(userRepository.deleteUser(user.getUserId())).thenReturn("User deleted successfully");
        String response = userService.deleteUser(user.getUserId());
        assertEquals("User deleted successfully", response, "User deleted successfully");

    }
    @Test
    public void getAllUsersTest() {
        List<UserProfile> mockUsers = new ArrayList<>();
        mockUsers.add(new UserProfile());
        mockUsers.add(new UserProfile());

        when(userRepository.getAllUsers(0,5)).thenReturn(mockUsers);
        List<UserProfile> result = userService.getAllUsers(0,5);
        verify(userRepository).getAllUsers(0,5);
        Assertions.assertEquals(2, result.size());
    }

    @Test
    public void fetchAllUsersTest() {
        List<UserProfile> mockUsers = new ArrayList<>();
        mockUsers.add(new UserProfile());
        mockUsers.add(new UserProfile());

        when(userRepository.fetchAllUsers()).thenReturn(mockUsers);
        List<UserProfile> result = userService.fetchAllUsers();
        verify(userRepository).fetchAllUsers();
        Assertions.assertEquals(2, result.size());
    }

    @Test
    public void getTotalUserCountTest() {
        List<UserProfile> mockUsers = new ArrayList<>();
        mockUsers.add(new UserProfile());
        mockUsers.add(new UserProfile());
        Mono<Long> repoCount = Mono.just(0L);
        when(userRepository.totalUsers()).thenReturn(repoCount);
        Mono<Long> serviceCount = userService.getTotalUsersCount();
        Assertions.assertEquals(repoCount, serviceCount);
    }



    @Test
    public void addUserTest() throws JsonProcessingException {
        UserProfile newUserProfile = new UserProfile(1, "Kumar", "student");

        when(userRepository.addUser(eq(newUserProfile)))
                .thenReturn(newUserProfile);

        UserProfile addedUserProfile = userService.addUser(newUserProfile);

        verify(userRepository).addUser(newUserProfile);
        assertNotNull(addedUserProfile);
        assertEquals("",newUserProfile.getUserId(), addedUserProfile.getUserId());
        assertEquals("",newUserProfile.getUserName(), addedUserProfile.getUserName());
        assertEquals("",newUserProfile.getUserRole(), addedUserProfile.getUserRole());
    }


}
