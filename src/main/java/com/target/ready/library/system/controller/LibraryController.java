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
            description = "Get all the books",
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
    public ResponseEntity<String> addBook(@RequestBody BookDto bookDto) throws JsonProcessingException {

        return new ResponseEntity<>(librarySystemService.addBook(bookDto),HttpStatus.CREATED);
    }

    @GetMapping("/book/{book_id}")
    @Operation(
            description = "Get book according to its id",
            responses = { @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json"
                    ))})
    public ResponseEntity<Book> findByBookId(@PathVariable("book_id") int bookId) {
        return new ResponseEntity<>(librarySystemService.findByBookId(bookId),HttpStatus.OK);
    }

    @GetMapping("/book/category/{category_name}")
    @Operation(
            description = "Get book according to its category",
            responses = { @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json"
                    ))})
    public ResponseEntity<List<Book>> findBookByCategoryName(@PathVariable("category_name") String categoryName) {
        return new ResponseEntity<>(librarySystemService.findBookByCategoryName(categoryName)
                ,HttpStatus.OK);
    }

    @GetMapping("books/{book_name}")
    @Operation(
            description = "Get book according to its name",
            responses = { @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json"
                    ))})
    public ResponseEntity<List<Book>> findByBookName(@PathVariable("book_name") String bookName){
        return new ResponseEntity<>(librarySystemService.findByBookName(bookName),HttpStatus.OK);

    }


    @DeleteMapping("book/{book_id}")
    @Operation(
            description = "Delete book from the database",
            responses = { @ApiResponse(
                    responseCode = "202",
                    content = @Content(
                            mediaType = "application/json"
                    ))})
    public ResponseEntity<String> deleteBook(@PathVariable("book_id") int bookId) {
        Book existingBook = librarySystemService.findByBookId(bookId);
        if (existingBook == null) {
            return new ResponseEntity<>("Book does not exist",HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(librarySystemService.deleteBook(bookId),HttpStatus.ACCEPTED);
        }

    }



    @PutMapping("/inventory/book/update/{book_id}")
    @Operation(
            description = "Update book details",
            responses = { @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json"
                    ))})
    public ResponseEntity<String> updateBookDetails(@PathVariable("book_id") int id, @RequestBody BookDto bookDto) {
        Book existingBook = librarySystemService.findByBookId(id);
        if (existingBook == null) {
            return new ResponseEntity<>("Book does not exist",HttpStatus.OK);
        } else {
            return new ResponseEntity<>(librarySystemService.updateBookDetails(id, bookDto),HttpStatus.OK);
        }
    }

    @PostMapping("inventory/issue/book/{book_id}/{student_id}")
    @Operation(
            description = "Issue book to the student",
            responses = { @ApiResponse(
                    responseCode = "201",
                    content = @Content(
                            mediaType = "application/json"
                    ))})
    public ResponseEntity<String> bookIssued(@PathVariable("book_id") int bookId,@PathVariable("student_id") int userId){
        return new ResponseEntity<>(librarySystemService.booksIssued(bookId,userId),HttpStatus.CREATED);
    }

    @PostMapping("inventory/return/book/{book_id}/{student_id}")
    @Operation(
            description = "Book returned by the student",
            responses = { @ApiResponse(
                    responseCode = "201",
                    content = @Content(
                            mediaType = "application/json"
                    ))})
    public ResponseEntity<Integer> bookReturned(@PathVariable("book_id") int bookId, @PathVariable("student_id") int userId){
        return new ResponseEntity<>(librarySystemService.bookReturned(bookId,userId),HttpStatus.CREATED);
    }

    @GetMapping("book/no_of_copies/{book_id}")
    @Operation(
            description = "No of copies of the given book present in the database",
            responses = { @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json"
                    ))})
    public ResponseEntity<Integer> getNoOfCopiesByBookId(@PathVariable("book_id") Integer bookId){
        return new ResponseEntity<>(librarySystemService.getNoOfCopiesByBookId(bookId),HttpStatus.OK);
    }


}



