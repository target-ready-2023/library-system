package com.target.ready.library.system.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.target.ready.library.system.dto.BookDto;
import com.target.ready.library.system.dto.IssueDto;
import com.target.ready.library.system.dto.BookDtoUpdate;
import com.target.ready.library.system.entity.Book;
import com.target.ready.library.system.entity.BookCategory;
import com.target.ready.library.system.entity.Inventory;
import com.target.ready.library.system.entity.UserCatalog;
import com.target.ready.library.system.exceptions.ResourceAlreadyExistsException;
import com.target.ready.library.system.exceptions.ResourceNotFoundException;
import com.target.ready.library.system.repository.BookImplementation;
import com.target.ready.library.system.repository.InventoryImplementation;
import com.target.ready.library.system.repository.UserImplementation;
import com.target.ready.library.system.service.CategoryService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import com.target.ready.library.system.service.LibrarySystemService;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.BindingResult;

import reactor.core.publisher.Mono;


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
    public void testGetAllBooksWithNegativePageNumber() {
        int pageNumber = -1;

        ResponseEntity<List<Book>> response = libraryController.getAllBooks(pageNumber);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.emptyList(), response.getBody());

        verify(librarySystemService, times(0)).getAllBooks(anyInt(), anyInt());
    }

    @Test
    public void getTotalBookCountTest() {
        List<Book> records = new ArrayList<Book>();
        records.add(new Book(1,
                "Five Point someone",
                "Semi-autobiographical"
                ,"Chetan Bhagat",2004));
        records.add(new Book(2,
                "The Silent Patient",
                "The dangers of unresolved or improperly treated mental illness","Alex Michaelides",2019)
        );
        Mono<Long> serviceResult=Mono.just(0L);
        when(librarySystemService.getTotalBookCount()).thenReturn(serviceResult);
        ResponseEntity<Mono<Long>> categoryResult=libraryController.getTotalBookCount();
        assertEquals(HttpStatus.OK, categoryResult.getStatusCode());
        assertEquals(serviceResult,categoryResult.getBody());
    }
    @Test
    public void bookIssuedTest(){
       IssueDto user = new IssueDto();
       user.setBookId(1);
       user.setStudentId(2);

       when(librarySystemService.booksIssued(user.getBookId(),user.getStudentId())).thenReturn(String.valueOf(user));
       ResponseEntity<String> response = libraryController.bookIssued(user);
       assertEquals(HttpStatus.CREATED, response.getStatusCode());
       assertNotNull(response);
    }

    @Test
    public void bookReturnedTest(){
        IssueDto user = new IssueDto();
        user.setBookId(1);
        user.setStudentId(2);

        when(librarySystemService.bookReturned(user.getBookId(),user.getStudentId())).thenReturn(user.getBookId(), user.getStudentId());
        libraryController.bookReturned(user);
        assertEquals(1, user.getBookId());
        assertEquals(2, user.getStudentId());
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

    @Test
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

        when(librarySystemService.findBookByCategoryName("Sci-Fi",0,5)).thenReturn(returnBooks);
        ResponseEntity<List<Book>> response = libraryController.findBookByCategoryName(bookCategory1.getCategoryName(),0);
        assertEquals(response.getBody(), returnBooks);
    }

    @Test
    public void testFindBookByCategoryNamePageNumberNegative() {
        String categoryName = "Mystery";
        int pageNumber = -1;

        ResponseEntity<List<Book>> response = libraryController.findBookByCategoryName(categoryName, pageNumber);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(librarySystemService, times(0)).findBookByCategoryName(anyString(), anyInt(), anyInt());
    }

    @Test
    public void getTotalBookCategoryCountTest() {
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

        Mono<Long> totalCount=Mono.just(0L);
        when(librarySystemService.getTotalBookCategoryCount("Sci-Fi")).thenReturn(totalCount);
        ResponseEntity<Mono<Long>> categoryCount=libraryController.getTotalBookCategoryCount("Sci-Fi");
        assertEquals(HttpStatus.OK, categoryCount.getStatusCode());
        assertEquals(totalCount,categoryCount.getBody());
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
        when(librarySystemService.deleteBook(2)).thenReturn("Book deleted successfully");

        ResponseEntity<String> response = libraryController.deleteBook(book.getBookId());
        assertEquals(response.getStatusCode(),HttpStatus.ACCEPTED);
        assertEquals(response.getBody(),"Book deleted successfully");
    }

    @Test
    public void addBookTest() throws JsonProcessingException {
        when(librarySystemService.addBook(new BookDto())).thenReturn(new BookDto());
        BindingResult bindingResult=null;
        ResponseEntity<?> response=libraryController.addBook(new BookDto(),bindingResult);
        assertEquals(new BookDto(),response.getBody());
    }
    @Test
    public void testAddBookWithJsonProcessingError() throws JsonProcessingException {
        BookDto bookDto = new BookDto();
        BindingResult bindingResult = mock(BindingResult.class);

        when(librarySystemService.addBook(bookDto)).thenThrow(JsonProcessingException.class);

        ResponseEntity<?> response = libraryController.addBook(bookDto, bindingResult);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred while processing your request.", response.getBody());

        verify(librarySystemService, times(1)).addBook(bookDto);
    }

    @Test
    void updateBookDetailsTest() throws JsonProcessingException {
        int bookId = 1;
        BookDtoUpdate bookDtoUpdate = new BookDtoUpdate();
        Book bookToUpdate = new Book();
        bookToUpdate.setBookId(1);
        bookToUpdate.setBookName("Think and grow rich");
        bookToUpdate.setBookDescription("A book guide on how to think to make money");
        bookToUpdate.setAuthorName("Napoleon Hill");
        bookToUpdate.setPublicationYear(1934);
        bookDtoUpdate.setBook(bookToUpdate);
        when(librarySystemService.findByBookId(bookId)).thenReturn(bookToUpdate);
        when(librarySystemService.updateBookDetails(bookId, bookDtoUpdate)).thenReturn(bookDtoUpdate);
        ResponseEntity<?> response = libraryController.updateBookDetails(bookDtoUpdate,null);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof BookDtoUpdate);
        BookDtoUpdate responseBody = (BookDtoUpdate) response.getBody();
        assertEquals(bookToUpdate.getBookName(), responseBody.getBook().getBookName());
        assertEquals(bookToUpdate.getAuthorName(), responseBody.getBook().getAuthorName());
    }





    @Test
    public void testGetNoOfCopiesByBookId() {
        int bookId = 123;
        int expectedNoOfCopies = 5;

        when(librarySystemService.getNoOfCopiesByBookId(bookId)).thenReturn(expectedNoOfCopies);

        ResponseEntity<Integer> response = libraryController.getNoOfCopiesByBookId(bookId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedNoOfCopies, response.getBody());

        verify(librarySystemService, times(1)).getNoOfCopiesByBookId(bookId);
    }

//    @Test
//    public void testUpdateBookDetailsBookNotFound() throws JsonProcessingException {
//        int bookId = 123;
//        BookDtoUpdate bookDtoUpdate = new BookDtoUpdate();
//        bookDtoUpdate.setBook(new Book()); // Set a valid Book object
//
//        when(librarySystemService.findByBookId(bookId)).thenReturn(null);
//
//        ResponseEntity<?> response = libraryController.updateBookDetails(bookDtoUpdate, mock(BindingResult.class));
//
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//        assertEquals("Book does not exist", response.getBody());
//
//        verify(librarySystemService, times(1)).findByBookId(bookId);
//        verify(librarySystemService, times(0)).updateBookDetails(anyInt(), any(BookDtoUpdate.class));
//    }
    @Test
    public void testUpdateBookDetailsWithJsonProcessingError() throws JsonProcessingException {
        BookDtoUpdate bookDtoUpdate = new BookDtoUpdate();
        bookDtoUpdate.setBook(new Book()); // Set a valid Book object

        int id = bookDtoUpdate.getBook().getBookId();
        when(librarySystemService.findByBookId(id)).thenReturn(new Book());
        when(librarySystemService.updateBookDetails(id, bookDtoUpdate)).thenThrow(JsonProcessingException.class);

        ResponseEntity<?> response = libraryController.updateBookDetails(bookDtoUpdate, mock(BindingResult.class));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred while processing your request", response.getBody());

        verify(librarySystemService, times(1)).findByBookId(id);
        verify(librarySystemService, times(1)).updateBookDetails(id, bookDtoUpdate);
    }

//    @Test
//    public void testFindByBookId()  {
//        int bookId = 123;
//        Inventory expectedInventory = new Inventory(bookId,6, 6);
//
//        when(librarySystemService.findByBookId(bookId)).thenReturn(expectedInventory);
//
//        ResponseEntity<?> response = libraryController.findByBookId(bookId);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(expectedInventory, response.getBody());
//
//        verify(librarySystemService, times(1)).findByBookId(bookId);
//    }



}




