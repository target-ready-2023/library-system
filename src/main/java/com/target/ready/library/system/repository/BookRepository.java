package com.target.ready.library.system.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.target.ready.library.system.dto.BookDto;
import com.target.ready.library.system.dto.BookDtoUpdate;
import com.target.ready.library.system.entity.Book;
import com.target.ready.library.system.exceptions.ResourceAlreadyExistsException;
import com.target.ready.library.system.exceptions.ResourceNotFoundException;
import org.springframework.dao.DataAccessException;
import reactor.core.publisher.Mono;

import java.util.List;


public interface BookRepository {
    public List<Book> findBookByCategoryName(String categoryName,int pageNumber,int pageSize) ;
    public List<Book> findByBookName(String bookName) throws ResourceNotFoundException;
    public List<Book> getAllBooks(int pageNumber, int pageSize);

    public Book addBook(BookDto bookDto) throws ResourceAlreadyExistsException, JsonProcessingException;

    public Book findByBookId(int bookId);
    //    public void deleteBook(int bookId);
//    public Book updateBookDetails(int bookId, BookDto bookDto);
    public void deleteBook(int bookId) throws ResourceNotFoundException, DataAccessException;
//    public Book updateBookDetails(int bookId, BookDto bookDto);
    public Book updateBookDetails(int bookId, BookDtoUpdate bookDtoUpdate) throws JsonProcessingException, ResourceAlreadyExistsException;
    public Mono<Long> totalBooks() ;
    public Mono<Long> countBooksByCategoryName(String categoryName) throws ResourceNotFoundException;

}
