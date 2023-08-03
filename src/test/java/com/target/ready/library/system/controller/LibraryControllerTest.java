package com.target.ready.library.system.controller;
import com.target.ready.library.system.entity.Book;
import com.target.ready.library.system.entity.BookCategory;
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
       ResponseEntity<String> response = libraryController.bookIssued(user.getBookId(),user.getUserId());
       assertEquals(HttpStatus.CREATED, response.getStatusCode());
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
    public void findByBookIdTest() {
        Book book = new Book();
        book.setBookId(1);
        book.setBookName("Five Point someone");
        book.setAuthorName("Chetan Bhagat");
        book.setBookDescription("Semi-autobiographical");
        book.setPublicationYear(2004);

        when(librarySystemService.findByBookId(book.getBookId())).thenReturn(book);
        Book response = libraryController.findByBookId(book.getBookId()).getBody();
        assertEquals(1, response.getBookId());
    }

    public void findBookByCategoryNameTest() {
        List<Book> books = new ArrayList<>();
        List<BookCategory> bookCategories = new ArrayList<>();
        List<Book> returnBooks = new ArrayList<>();
        Book book1 = new Book(1,
                "Harry Potter and the Philosopher's Stone",
                "Harry Potter, a young wizard who discovers his magical heritage on his eleventh birthday, when he receives a letter of acceptance to Hogwarts School of Witchcraft and Wizardry."
                , "J. K. Rowling", 1997);
        books.add(book1);
        BookCategory bookCategory1 = new BookCategory();
        bookCategory1.setCategoryName("Fiction");
        bookCategory1.setBookId(1);
        bookCategory1.setId(1);
        bookCategories.add(bookCategory1);

        Book book2 = new Book(2,
                "The Immortals of Meluha",
                "follows the story of a man named Shiva, who lives in the Tibetan region – Mount Kailash."
                , "Amish Tripathi", 2010);
        books.add(book2);
        BookCategory bookCategory2 = new BookCategory();
        bookCategory2.setCategoryName("Sci-Fi");
        bookCategory2.setBookId(2);
        bookCategory2.setId(2);
        bookCategories.add(bookCategory2);

        when(librarySystemService.findBookByCategoryName("Sci-Fi")).thenReturn(returnBooks);
        ResponseEntity<List<Book>> response = libraryController.findBookByCategoryName(bookCategory1.getCategoryName());
        assertEquals(response.getBody(), returnBooks);
    }

    @Test
    public void findByBookNameTest(){
        List<Book> books = new ArrayList<>();
        Book book1=new Book(1,
                "The Hound of Death",
                "A young Englishman visiting Cornwall finds himself delving into the legend of a Belgian nun who is living as a refugee in the village."
                ,"Agatha Christie",1933);
        books.add(book1);
        Book book2=new Book(2,
                "The Adventure of Dancing Men",
                "The little dancing men are at the heart of a mystery which seems to be driving his young wife Elsie Patrick to distraction."
                ,"Sir Arthur Conan Doyle",1903);
        books.add(book2);
        when(librarySystemService.findByBookName("The Hound of Death")).thenReturn(books);
        ResponseEntity<List<Book>> response = libraryController.findByBookName(book1.getBookName());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(books, response.getBody());
    }

    @Test
    public void deleteBookTest() {

        Book book = new Book();
        book.setBookId(2);
        book.setBookName("Day of the Jackal");
        book.setBookDescription("Masterpiece");
        book.setAuthorName("Frederick Forsyth");
        book.setPublicationYear(1981);

        when(librarySystemService.findByBookId(2)).thenReturn(book);
        when(librarySystemService.deleteBook(2)).thenReturn("Book deleted Successfully!");

        ResponseEntity<String> response = libraryController.deleteBook(book.getBookId());
        assertEquals(response.getStatusCode(),HttpStatus.ACCEPTED);
        assertEquals(response.getBody(),"Book deleted Successfully!");
    }

}
