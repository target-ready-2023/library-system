package com.target.ready.library.system.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.target.ready.library.system.dto.BookDto;
import com.target.ready.library.system.entity.Book;
import com.target.ready.library.system.entity.UserCatalog;
import com.target.ready.library.system.exceptions.ClientErrorException;
import com.target.ready.library.system.exceptions.ResourceNotFoundException;
import com.target.ready.library.system.service.LibrarySystemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

@RestController
@Validated
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
    public ResponseEntity<String> addBook(@Valid @RequestBody BookDto bookDto,BindingResult bindingResult) {
        try {
            return new ResponseEntity<>(librarySystemService.addBook(bookDto), HttpStatus.CREATED);
        } catch (ClientErrorException clientError) {
            return new ResponseEntity<>(clientError.getMessage(), HttpStatus.CONFLICT); // Customize status code and response body as needed
        }
        catch (JsonProcessingException e) {
            return new ResponseEntity<>("An error occurred while processing your request.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
        try {
            Book existingBook = librarySystemService.findByBookId(bookId);
            if (existingBook == null) {
                throw new ResourceNotFoundException("Book not found");
            } else {
                librarySystemService.deleteBook(bookId);
                return new ResponseEntity<>("Book deleted successfully", HttpStatus.ACCEPTED);
            }
        } catch (ResourceNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        } catch (ClientErrorException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred", ex);
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

    @PostMapping("inventory/issue/book")
    @Operation(
            description = "Issue book to the student",
            responses = { @ApiResponse(
                    responseCode = "201",
                    content = @Content(
                            mediaType = "application/json"
                    ))})
    public ResponseEntity<String> bookIssued(@Valid @RequestBody UserCatalog userCatalog){
        return new ResponseEntity<>(librarySystemService.booksIssued(userCatalog.getBookId(), userCatalog.getUserId())
                ,HttpStatus.CREATED);
    }


    @PostMapping("inventory/return/book")
    @Operation(
            description = "Book returned by the student",
            responses = { @ApiResponse(
                    responseCode = "201",
                    content = @Content(
                            mediaType = "application/json"
                    ))})
    public ResponseEntity<Integer> bookReturned(@Valid @RequestBody UserCatalog userCatalog){
        return new ResponseEntity<>(librarySystemService.bookReturned(userCatalog.getBookId(), userCatalog.getUserId())
                ,HttpStatus.CREATED);
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



