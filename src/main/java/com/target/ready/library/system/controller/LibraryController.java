package com.target.ready.library.system.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.target.ready.library.system.dto.BookDto;
import com.target.ready.library.system.dto.IssueDto;
import com.target.ready.library.system.dto.BookDtoUpdate;
import com.target.ready.library.system.entity.Book;
import com.target.ready.library.system.entity.Inventory;
import com.target.ready.library.system.entity.UserCatalog;
import com.target.ready.library.system.exceptions.DataAccessException;
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

import reactor.core.publisher.Mono;

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
            responses = {@ApiResponse(
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

    @GetMapping("books_directory/total_count")
    @Operation(
            description = "Get all the books count",
            responses = {@ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json"
                    ))})
    public ResponseEntity<Mono<Long>> getTotalBookCount() {
        try {
            Mono<Long> totalCount = librarySystemService.getTotalBookCount();
            return new ResponseEntity<>(totalCount, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/inventory/books")
    @Operation(
            description = "Addition of books and its details",
            responses = {@ApiResponse(
                    responseCode = "201",
                    content = @Content(
                            mediaType = "application/json"
                    ))})
    public ResponseEntity<?> addBook(@Valid @RequestBody BookDto bookDto, BindingResult bindingResult) {
        try {
            return new ResponseEntity<>(librarySystemService.addBook(bookDto), HttpStatus.CREATED);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("An error occurred while processing your request.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/book/{book_id}")
    @Operation(
            description = "Get book according to its id",
            responses = {@ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json"
                    ))})
    public ResponseEntity<Book> findByBookId(@PathVariable("book_id") int bookId) {
        try {
            return new ResponseEntity<>(librarySystemService.findByBookId(bookId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/book/category/{category_name}")
    @Operation(
            description = "Get book according to its category",
            responses = {@ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json"
                    ))})
    public ResponseEntity<List<Book>> findBookByCategoryName(@PathVariable("category_name") String categoryName, @RequestParam(value = "page_number", defaultValue = "0", required = false) Integer pageNumber) {
        int pageSize = 5;
        if (pageNumber < 0)
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(librarySystemService.findBookByCategoryName(categoryName, pageNumber, pageSize)
                , HttpStatus.OK);
    }

    @GetMapping("/books/category/total_count/{categoryName}")
    @Operation(
            description = "Get number of books according to its category",
            responses = {@ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json"
                    ))})
    public ResponseEntity<Mono<Long>> getTotalBookCategoryCount(@PathVariable String categoryName) {
        try {
            Mono<Long> totalCount = librarySystemService.getTotalBookCategoryCount(categoryName);
            return new ResponseEntity<>(totalCount, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("books/{book_name}")
    @Operation(
            description = "Get book according to its name",
            responses = {@ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json"
                    ))})
    public ResponseEntity<List<Book>> findByBookName(@PathVariable("book_name") String bookName) {
        try {
            return new ResponseEntity<>(librarySystemService.findByBookName(bookName), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        }

    }


    @DeleteMapping("book/{book_id}")
    @Operation(
            description = "Delete book from the database",
            responses = {@ApiResponse(
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
        } catch (DataAccessException ex) {
            return new ResponseEntity<>("'Couldn't access database", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/inventory/book/update")
    @Operation(
            description = "Update book details",
            responses = {@ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json"
                    ))})
    public ResponseEntity<?> updateBookDetails(@RequestBody @Valid BookDtoUpdate bookDtoUpdate, BindingResult bindingResult) {
        int id = bookDtoUpdate.getBook().getBookId();
        try {
            Book existingBook = librarySystemService.findByBookId(id);
            if (existingBook == null) {
                return new ResponseEntity<>("Book does not exist", HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(librarySystemService.updateBookDetails(id, bookDtoUpdate), HttpStatus.OK);
            }
        } catch (JsonProcessingException je) {
            return new ResponseEntity<>("An error occurred while processing your request", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("inventory/issue/book")
    @Operation(
            description = "Issue book to the student",
            responses = {@ApiResponse(
                    responseCode = "201",
                    content = @Content(
                            mediaType = "application/json"
                    ))})
    public ResponseEntity<String> bookIssued(@RequestBody IssueDto issueDto) {
        return new ResponseEntity<>(librarySystemService.booksIssued(issueDto.getBookId(), issueDto.getStudentId()), HttpStatus.CREATED);
    }


    @PostMapping("inventory/return/book")
    @Operation(
            description = "Book returned by the student",
            responses = {@ApiResponse(
                    responseCode = "201",
                    content = @Content(
                            mediaType = "application/json"
                    ))})

    public ResponseEntity<Integer> bookReturned(@RequestBody IssueDto issueDto) {

        return new ResponseEntity<>(librarySystemService.bookReturned(issueDto.getBookId(), issueDto.getStudentId())
                , HttpStatus.CREATED);
    }


    @GetMapping("book/no_of_copies/{book_id}")
    @Operation(
            description = "No of copies of the given book present in the database",
            responses = {@ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json"
                    ))})

    public ResponseEntity<Integer> getNoOfCopiesByBookId(@PathVariable("book_id") Integer bookId) {
        return new ResponseEntity<>(librarySystemService.getNoOfCopiesByBookId(bookId), HttpStatus.OK);
    }

    @GetMapping("inventory/book/{book_id}")
    @Operation(
            description = "No of copies available of the  given book",
            responses = {@ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json"
                    ))})
    public ResponseEntity<Inventory> findByBookId(@PathVariable("book_id") Integer bookId) {
        return new ResponseEntity<>(librarySystemService.findByBookId(bookId), HttpStatus.OK);
    }


}



