package com.target.ready.library.system.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.target.ready.library.system.Entity.Book;
import com.target.ready.library.system.Exceptions.ResourceNotFoundException;
import com.target.ready.library.system.Service.LibrarySystemService;
import org.springframework.expression.spel.ast.NullLiteral;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("library_system/v1")
public class LibraryController {
    private final LibrarySystemService librarySystemService;
    LibraryController(LibrarySystemService librarySystemService){
        this.librarySystemService=librarySystemService;
    }
    @GetMapping("books")
    public List<Book> getAllBooks(){

        return librarySystemService.getAllBooks();
    }

    @PostMapping("inventory/books")
    public String addBook(@RequestBody Book book) throws JsonProcessingException {
        String categoryName=book.getCategoryName();
        return librarySystemService.addBook(book,categoryName);
    }

    @GetMapping("book/{bookId}")
    public Book findByBookId(@PathVariable int bookId){
        return librarySystemService.findByBookId(bookId);
    }
    @GetMapping("book/category/{categoryName}")
    public List<Book> findBookByCategoryName(@PathVariable String categoryName){
        return librarySystemService.findBookByCategoryName(categoryName);
    }

    @DeleteMapping("book/{bookId}")
    public String deleteBook(@PathVariable("bookId") int bookId) {

        return librarySystemService.deleteBook(bookId);
    }

    @PutMapping("inventory/book_update/{id}")
    public ResponseEntity<?> updateBookDetails(@PathVariable("id") int id, @RequestBody Book book) {
        Book existingBook = librarySystemService.findByBookId(id);
        if (existingBook == null) {
            return new ResponseEntity<>("Book with id: " + id + ", does not exist in database", HttpStatus.NOT_FOUND);
        } else {
            Book updatedBook = librarySystemService.updateBookDetails(id, book);
            return new ResponseEntity<>(updatedBook, HttpStatus.OK);
        }
    }



    }
