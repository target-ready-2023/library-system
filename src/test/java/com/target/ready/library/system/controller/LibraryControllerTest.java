package com.target.ready.library.system.controller;
import com.target.ready.library.system.entity.Book;
import com.target.ready.library.system.repository.BookImplementation;
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
