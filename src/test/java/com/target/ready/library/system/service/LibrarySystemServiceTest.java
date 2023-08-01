package com.target.ready.library.system.service;

import com.target.ready.library.system.entity.Book;
import com.target.ready.library.system.entity.BookCategory;
import com.target.ready.library.system.repository.BookCategoryRepository;
import com.target.ready.library.system.repository.BookRepository;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;

import java.util.*;

@SpringBootTest(classes = {LibrarySystemServiceTest.class})
public class LibrarySystemServiceTest {

    @Mock
    BookRepository bookRepository;

    @Mock
    BookCategoryRepository bookCategoryRepository;

    @Mock
    CategoryService categoryService;

    @InjectMocks
    LibrarySystemService librarySystemService;

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
    public void deleteBookTest() {
        List<Book> books = new ArrayList<>();
        List<BookCategory> categories = new ArrayList<>();

        Book book1 = new Book();
        book1.setBookId(1);
        book1.setBookName("Life of Suraj 1");
        book1.setBookDescription("Masterpiece");
        book1.setAuthorName("Suraj");
        book1.setPublicationYear(2024);
        books.add(book1);

        BookCategory category1= new BookCategory();
        category1.setId(3);
        category1.setBookId(1);
        category1.setCategoryName("Thriller");
        categories.add(category1);

        Book book2 = new Book();
        book2.setBookId(2);
        book2.setBookName("Life of Suraj 2");
        book2.setBookDescription("Masterpiece");
        book2.setAuthorName("Suraj");
        book2.setPublicationYear(2024);
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

}
