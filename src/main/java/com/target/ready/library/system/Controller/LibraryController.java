package com.target.ready.library.system.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.target.ready.library.system.Entity.Book;
import com.target.ready.library.system.Entity.BookDto;
import com.target.ready.library.system.Service.LibrarySystemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("library_system/v1")
public class LibraryController {
    private final LibrarySystemService librarySystemService;
    LibraryController(LibrarySystemService librarySystemService){
        this.librarySystemService=librarySystemService;
    }
    @GetMapping("getBooks")
    public List<Book> getAllBooks(){
        return librarySystemService.getAllBooks();
    }

    @PostMapping("inventory/books")
    public String addBook(@RequestBody BookDto bookDto) throws JsonProcessingException {

        return librarySystemService.addBook(bookDto);
    }

    @GetMapping("book/{bookId}")
    public Book findByBookId(@PathVariable int bookId){
        return librarySystemService.findByBookId(bookId);
    }



}
