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
    public void findByBookNameTest() {
        List<Book> books = new ArrayList<>();
        Book book1 = new Book(1,
                "The Hound of Death",
                "A young Englishman visiting Cornwall finds himself delving into the legend of a Belgian nun who is living as a refugee in the village."
                , "Agatha Christie", 1933);
        books.add(book1);
        Book book2 = new Book(2,
                "The Adventure of Dancing Men",
                "The little dancing men are at the heart of a mystery which seems to be driving his young wife Elsie Patrick to distraction."
                , "Sir Arthur Conan Doyle", 1903);
        books.add(book2);
        when(librarySystemService.findByBookName("The Hound of Death")).thenReturn(books);
        ResponseEntity<List<Book>> response = libraryController.findByBookName(book1.getBookName());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(books, response.getBody());
    }

}
