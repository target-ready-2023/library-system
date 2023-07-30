package com.target.ready.library.system.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.target.ready.library.system.dto.BookDto;
import com.target.ready.library.system.entity.Book;
import com.target.ready.library.system.service.LibrarySystemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("library_system/v1")
public class LibraryController {
    private final LibrarySystemService librarySystemService;

    LibraryController(LibrarySystemService librarySystemService) {
        this.librarySystemService = librarySystemService;
    }

    @GetMapping("/books_directory")
    @Operation(
            description = "Get all the books on the given range of pages (range given by the backend)",
            responses = { @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json"
                    ))})
    public ResponseEntity<List<Book>> getAllBooks(@RequestParam(value = "page_number", defaultValue = "0", required = false) Integer pageNumber) {
        List<Book> books;
        int pageSize = 5;
        try {
            if (pageNumber < 0) {
                return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
            }
            books = librarySystemService.getAllBooks(pageNumber, pageSize);
            return new ResponseEntity<>(books, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        }
    }

    @PostMapping("/inventory/books")
    @Operation(
            description = "Addition of books and its details",
            responses = { @ApiResponse(
                    responseCode = "201",
                    content = @Content(
                            mediaType = "application/json"
                    ))})
    public String addBook(@RequestBody BookDto bookDto) throws JsonProcessingException {

        return librarySystemService.addBook(bookDto);
    }

    @GetMapping("/book/{bookId}")
    @Operation(
            description = "Get book according to its id",
            responses = { @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json"
                    ))})
    public Book findByBookId(@PathVariable int bookId) {
        return librarySystemService.findByBookId(bookId);
    }



    @GetMapping("/book/category/{categoryName}")
    public List<Book> findBookByCategoryName(@PathVariable String categoryName) {
        return librarySystemService.findBookByCategoryName(categoryName);
    }
    @GetMapping("books/{bookName}")
    @Operation(
            description = "Get book according to its name",
            responses = { @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json"
                    ))})
    public List<Book> findByBookName(@PathVariable String bookName){
        return librarySystemService.findByBookName(bookName);

    }


    @DeleteMapping("book/{bookId}")
    public String deleteBook(@PathVariable("bookId") int bookId) {
        Book existingBook = librarySystemService.findByBookId(bookId);
        if (existingBook == null) {
            return "Book does not exist";
        } else {
            return librarySystemService.deleteBook(bookId);
        }

    }



    @PutMapping("/inventory/book/update/{id}")
    @Operation(
            description = "Update book API",
            responses = { @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json"
                    ))})
    public String updateBookDetails(@PathVariable("id") int id, @RequestBody BookDto bookDto) {
        Book existingBook = librarySystemService.findByBookId(id);
        if (existingBook == null) {
            return "Book does not exist";
        } else {
            return librarySystemService.updateBookDetails(id, bookDto);
        }
    }



}



