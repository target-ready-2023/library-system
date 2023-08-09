package com.target.ready.library.system.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.target.ready.library.system.dto.BookDto;
import com.target.ready.library.system.entity.*;
import com.target.ready.library.system.exceptions.ResourceNotFoundException;
import com.target.ready.library.system.repository.BookCategoryRepository;
import com.target.ready.library.system.repository.BookRepository;
import com.target.ready.library.system.repository.InventoryRepository;
import com.target.ready.library.system.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;

import java.util.*;

@SpringBootTest(classes = {LibrarySystemServiceTest.class})
public class LibrarySystemServiceTest {


    BookRepository bookRepository;

    InventoryRepository inventoryRepository;

    UserRepository userRepository;

    @Mock
    CategoryService categoryService;

    @Mock
    BookCategoryRepository bookCategoryRepository;

    @InjectMocks
    LibrarySystemService librarySystemService;

    @InjectMocks
    UserService userService;

    @BeforeEach
    public void setUp() {
        // Initialize the mocked repositories\
        bookRepository = mock(BookRepository.class);
        inventoryRepository = mock(InventoryRepository.class);
        userRepository = mock(UserRepository.class);

        // Inject the mocked repositories into the librarySystemService
        librarySystemService = new LibrarySystemService(bookRepository, inventoryRepository, userRepository, categoryService);
    }

    @Test
    public void findAllBooksTest(){
        List<Book> records = new ArrayList<Book>();
        records.add(new Book(1,
                "Five Point someone",
                "Semi-autobiographical"
                ,"Chetan Bhagat",2004));
        records.add(new Book(2,
                "The Silent Patient",
                "The dangers of unresolved or improperly treated mental illness","Alex Michaelides",2019)
        );

        when(bookRepository.getAllBooks(0,5)).thenReturn(records);
        List<Book> response=librarySystemService.getAllBooks(0,5);
        assertEquals(2, response.size());
    }

    @Test
    public void booksIssuedTest(){
        int bookId=1;
        int userId = 1;

        Inventory inventory= new Inventory();
        inventory.setInvBookId(1);
        inventory.setNoOfBooksLeft(5);
        inventory.setNoOfCopies(5);
        when(inventoryRepository.findByBookId(inventory.getInvBookId())).thenReturn(inventory);
        inventory.setNoOfBooksLeft(inventory.getNoOfBooksLeft()-1);

        UserCatalog user = new UserCatalog();
        user.setBookId(1);
        user.setUserId(1);
        when(userRepository.addUserCatalog(user)).thenReturn(user);

        String response = librarySystemService.booksIssued(1, 1);
        assertEquals("Book issued", response);
    }

    @Test
    public void bookReturnedTest(){
        UserCatalog user1 = new UserCatalog();
        user1.setBookId(1);
        user1.setUserId(1);

        UserCatalog user2 = new UserCatalog();
        user2.setBookId(2);
        user2.setUserId(1);

        List<Integer> users = new ArrayList<>();
        users.add(user1.getBookId());
        users.add(user2.getBookId());

        when(userRepository.findBooksByUserId(1)).thenReturn(users);

        Inventory inventory = new Inventory();
        inventory.setInvBookId(1);
        inventory.setNoOfBooksLeft(5);
        inventory.setNoOfCopies(5);
        when(inventoryRepository.findByBookId(1)).thenReturn(inventory);
        when(inventoryRepository.addInventory(inventory)).thenReturn(inventory);
        when(userRepository.deleteBookByUserId(1,1)).thenReturn(1);
        Integer response = librarySystemService.bookReturned(1,1);
        assertEquals(1, response);
    }

    @Test
    public void findByBookIdTest(){
        Book book = new Book();
        book.setBookId(1);
        book.setBookName("Five Point someone");
        book.setAuthorName("Chetan Bhagat");
        book.setBookDescription("Semi-autobiographical");
        book.setPublicationYear(2004);

        when(bookRepository.findByBookId(1)).thenReturn(book);
        Book response = librarySystemService.findByBookId(1);
        assertEquals(book, response);

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

        when(bookRepository.findBookByCategoryName("Sci-Fi")).thenReturn(returnBooks);
        List<Book> response = librarySystemService.findBookByCategoryName(bookCategory1.getCategoryName());
        assertEquals(response, returnBooks);
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

        when(bookRepository.findByBookName("The Hound of Death")).thenReturn(books);
        List<Book> result = librarySystemService.findByBookName(book1.getBookName());
        assertEquals(books, result);
    }

    @Test
    public void deleteBookTest() {
        List<Book> books = new ArrayList<>();
        List<BookCategory> categories = new ArrayList<>();

        Book book1 = new Book();
        book1.setBookId(1);
        book1.setBookName("Day of the Jackal");
        book1.setBookDescription("Masterpiece");
        book1.setAuthorName("Frederick Forsyth");
        book1.setPublicationYear(1981);
        books.add(book1);

        BookCategory category1= new BookCategory();
        category1.setId(3);
        category1.setBookId(1);
        category1.setCategoryName("Thriller");
        categories.add(category1);

        Book book2 = new Book();
        book2.setBookId(2);
        book2.setBookName("Angels and Demons");
        book2.setBookDescription("Masterpiece");
        book2.setAuthorName("Dan Brown");
        book2.setPublicationYear(2007);
        books.add(book2);

        BookCategory category2= new BookCategory();
        category2.setId(4);
        category2.setBookId(2);
        category2.setCategoryName("Suspense");
        categories.add(category2);

        doAnswer((invocation) -> {
            int id=invocation.getArgument(0);
            books.removeIf(book->book.getBookId()==id);
            return null;
        }).when(bookRepository).deleteBook(1);

        doAnswer((i) -> {
            categories.removeIf(category -> category.getId() == 1);
            return null;
        }).when(categoryService).deleteBookCategory(1);

        librarySystemService.deleteBook(1);
        assertEquals(books.size(),1);
    }


    @Test
    public void addBookTest() throws JsonProcessingException,ResourceNotFoundException {
        Book book = new Book();
        book.setBookId(1);
        book.setBookName("Five Point someone");
        book.setAuthorName("Chetan Bhagat");
        book.setBookDescription("Semi-autobiographical");
        book.setPublicationYear(2004);

        BookDto bookDto=new BookDto();
        bookDto.setBook(book);
        List<String> categoryNames=new ArrayList<>();
        categoryNames.add("Fiction");
        categoryNames.add("Horror");
        bookDto.setCategoryNames(categoryNames);
        bookDto.setNoOfCopies(5);
        when(bookRepository.addBook(bookDto)).thenReturn(book);
        Inventory inventory=new Inventory();
        inventory.setInvBookId(book.getBookId());
        inventory.setNoOfCopies(bookDto.getNoOfCopies());
        inventory.setNoOfBooksLeft(bookDto.getNoOfCopies());
        when(inventoryRepository.addInventory(inventory)).thenReturn(inventory);
        when(categoryService.findCategoryBycategoryName("Fiction")).thenThrow(new ResourceNotFoundException("Category Not found"));
//        when(categoryService.addCategory(new Category())).thenReturn("Category Added Successfully");
//        when(categoryService.addBookCategory(new BookCategory())).thenReturn("Category added to the book successfully");
//        String response=librarySystemService.addBook(bookDto);
//        assertEquals("Book Added Successfully",response);


    }
}
