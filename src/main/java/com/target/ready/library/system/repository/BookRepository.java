package com.target.ready.library.system.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.target.ready.library.system.dto.BookDto;
import com.target.ready.library.system.entity.Book;
import com.target.ready.library.system.exceptions.ResourceAlreadyExistsException;

import java.util.List;


public interface BookRepository {
    public List<Book> findBookByCategoryName(String categoryName);
    public List<Book> findByBookName(String bookName);
    public List<Book> getAllBooks(int pageNumber, int pageSize);
    public Book addBook(BookDto bookDto) throws ResourceAlreadyExistsException, JsonProcessingException;
    public Book findByBookId(int bookId);
//    public void deleteBook(int bookId);
//    public Book updateBookDetails(int bookId, BookDto bookDto);
    public void deleteBook(int bookId);
    public Book updateBookDetails(int bookId, BookDto bookDto);

}
