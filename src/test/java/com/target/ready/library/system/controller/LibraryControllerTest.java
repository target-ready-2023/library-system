package com.target.ready.library.system.controller;
import com.target.ready.library.system.entity.Book;
import com.target.ready.library.system.entity.Inventory;
import com.target.ready.library.system.entity.UserCatalog;
import com.target.ready.library.system.repository.BookImplementation;
import com.target.ready.library.system.repository.InventoryImplementation;
import com.target.ready.library.system.repository.UserImplementation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import com.target.ready.library.system.service.LibrarySystemService;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(classes = {LibraryControllerTest.class})
public class LibraryControllerTest {
    @Mock
    LibrarySystemService librarySystemService;

    @InjectMocks
    LibraryController libraryController;

    @Mock
    BookImplementation bookRepository;

    @Mock
    InventoryImplementation inventoryRepository;

    @Mock
    UserImplementation userRepository;

    @Test
    public void testGetAllBooks() throws Exception{
        List<Book>  records = new ArrayList<Book>();
        records.add(new Book(1,
                "Five Point someone",
                "Semi-autobiographical"
                ,"Chetan Bhagat",2004));
        records.add(new Book(2,
                "The Silent Patient",
                "The dangers of unresolved or improperly treated mental illness","Alex Michaelides",2019)
        );

        when(librarySystemService.getAllBooks(0,5)).thenReturn(records);
        ResponseEntity<List<Book>> response = libraryController.getAllBooks(0);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());

    }
    @Test
    public void bookIssuedTest(){
       UserCatalog user = new UserCatalog();
       user.setBookId(1);
       user.setUserId(2);

       when(librarySystemService.booksIssued(user.getBookId(),user.getUserId())).thenReturn(String.valueOf(user));

       String response = libraryController.bookIssued(user.getBookId(),user.getUserId());
       ResponseEntity<String> responseEntity = new ResponseEntity<String>(response,HttpStatus.OK);
       assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
       assertNotNull(response);
    }

    @Test
    public void bookReturnedTest(){
        UserCatalog user = new UserCatalog();
        user.setBookId(1);
        user.setUserId(2);

        when(librarySystemService.bookReturned(user.getBookId(),user.getUserId())).thenReturn(user.getBookId(), user.getUserId());
        libraryController.bookReturned(user.getBookId(),user.getUserId());
        assertEquals(1, user.getBookId());
        assertEquals(2, user.getUserId());
    }

    @Test
    public void deleteBookTest() {

        Book book = new Book();
        book.setBookId(2);
        book.setBookName("Life of Suraj");
        book.setBookDescription("Masterpiece");
        book.setAuthorName("Suraj");
        book.setPublicationYear(2024);

        when(librarySystemService.findByBookId(2)).thenReturn(book);
        when(librarySystemService.deleteBook(2)).thenReturn("Book deleted Successfully!");

        String response = libraryController.deleteBook(book.getBookId());
        assertEquals(response,"Book deleted Successfully!");
    }

}
