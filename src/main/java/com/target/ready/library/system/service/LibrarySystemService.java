package com.target.ready.library.system.service;


import com.target.ready.library.system.dto.BookDto;
import com.target.ready.library.system.entity.Book;
import com.target.ready.library.system.entity.BookCategory;
import com.target.ready.library.system.entity.Category;
import com.target.ready.library.system.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service

public class LibrarySystemService {
//    @Value("${library.baseUrl}")
//    private String libraryBaseUrl;
    @Autowired
    CategoryService categoryService;

    private final BookRepository bookRepository;

    @Autowired
    public LibrarySystemService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks(int pageNumber, int pageSize) {
        return bookRepository.getAllBooks(pageNumber,pageSize);
    }

    @Transactional
    public String addBook(BookDto bookDto) {
            Book book = bookRepository.addBook(bookDto);
            int bookId = book.getBookId();
            List<String> categoryNames = bookDto.getCategoryNames();
            for (String eachCategoryName : categoryNames) {

                Category category = categoryService.findCategoryBycategoryName(eachCategoryName);
                if (category == null) {
                    Category category1 = new Category();
                    category1.setCategoryName(eachCategoryName);
                    categoryService.addCategory(category1);
                }

                BookCategory bookCategory = new BookCategory();
                bookCategory.setBookId(bookId);
                bookCategory.setCategoryName(eachCategoryName);
                categoryService.addBookCategory(bookCategory);
            }
            return "Book Added Successfully";
    }


    public Book findByBookId(int bookId) {
        Book book = bookRepository.findByBookId(bookId);
        return book;
    }


        public List<Book> findBookByCategoryName(String categoryName) {
        List<Book> bookList= bookRepository.findBookByCategoryName(categoryName);
        return bookList;
    }

    public List<Book> findByBookName(String bookName){
       List<Book> books = bookRepository.findByBookName(bookName);
       return  books;
    }

    public String deleteBook(int bookId) {
        try {
            bookRepository.deleteBook(bookId);
            categoryService.deleteBookCategory(bookId);
            return "Book deleted Successfully!";
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete book", e);
        }
    }



    @Transactional
    public String updateBookDetails(int bookId, BookDto bookDto) {
        try {
            bookRepository.updateBookDetails(bookId, bookDto);
            categoryService.deleteBookCategory(bookId);
            List<String> categoryNames = bookDto.getCategoryNames();
            for (String categoryName : categoryNames) {
                Category category1 = new Category();
                category1.setCategoryName(categoryName);
                categoryService.addCategory(category1);
                BookCategory bookCategory = new BookCategory();
                bookCategory.setBookId(bookId);
                bookCategory.setCategoryName(categoryName);
                categoryService.addBookCategory(bookCategory);
            }
            return "Book updated" ;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update book details", e);
        }
    }


}




