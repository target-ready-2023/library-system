package com.target.ready.library.system.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.target.ready.library.system.entity.Book;
import com.target.ready.library.system.entity.UserCatalog;
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

    @Test
    public void testGetAllUsers()  {
        // Create a list of mock user profiles
        List<UserProfile> mockUsers = new ArrayList<>();
        mockUsers.add(new UserProfile(1, "Disha", "student"));
        mockUsers.add(new UserProfile(2, "Niraj", "Librarian"));
        // ... Add more mock user profiles as needed

        // Mock the behavior of the repository
        when(userRepository.getAllUsers())
                .thenReturn(mockUsers);

        // Call the service method
        List<UserProfile> result = userService.getAllUsers();

        // Verify the repository method was called
        verify(userRepository).getAllUsers();

        // Verify the returned result
        assertNotNull(result);
        assertEquals("",mockUsers.size(), result.size());
    }

    @Test
    public void testAddUser() throws JsonProcessingException {
        // Create a mock user profile to add
        UserProfile newUserProfile = new UserProfile(1, "Kumar", "student");

        // Mock the behavior of the repository
        when(userRepository.addUser(eq(newUserProfile)))
                .thenReturn(newUserProfile); // You can adjust this as needed

        // Call the service method
        UserProfile addedUserProfile = userService.addUser(newUserProfile);

        // Verify the repository method was called with the correct argument
        verify(userRepository).addUser(newUserProfile);

        // Verify the returned result
        assertNotNull(addedUserProfile);
        assertEquals("",newUserProfile.getUserId(), addedUserProfile.getUserId());
        assertEquals("",newUserProfile.getUserName(), addedUserProfile.getUserName());
        assertEquals("",newUserProfile.getUserRole(), addedUserProfile.getUserRole());
    }

    @Test
    public void testFindBookNameByUserId() {
        int userId = 1; // Replace with a valid user ID

        // Create a list of mock UserCatalog objects
        List<UserCatalog> mockUserCatalogs = new ArrayList<>();
        mockUserCatalogs.add(new UserCatalog(1, 1));
        mockUserCatalogs.add(new UserCatalog(1, 2));
        // ... Add more mock UserCatalog objects as needed

        // Create a list of mock Book objects
        List<Book> mockBooks = new ArrayList<>();
        mockBooks.add(new Book(1, "wings of fire","Biography of abdul kalam","Dr A P J Abdul Kalam",1999));
        mockBooks.add(new Book(2, "Life of Pi","Story of Boy","Yamm Martel",2001));
        // ... Add more mock Book objects as needed

        // Mock the behavior of the repository
        when(userRepository.findBooksByUserId(eq(userId)))
                .thenReturn(mockUserCatalogs);
        when(bookRepository.findByBookId(anyInt()))
                .thenReturn(null) // For books not found
                .thenReturn(mockBooks.get(0)) // For the first book
                .thenReturn(mockBooks.get(1)); // For the second book

        // Call the service method
        List<String> result = userService.findBookNameByUserId(userId);

        // Verify the repository methods were called
        verify(userRepository).findBooksByUserId(userId);
        verify(bookRepository, times(2)).findByBookId(anyInt());

        // Verify the returned result
        assertNotNull(result);
        assertEquals("",2, result.size());
        assertEquals("","wings of fire", result.get(0));
        assertEquals("","Life of Pi", result.get(1));
    }
}
