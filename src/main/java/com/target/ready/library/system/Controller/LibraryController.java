package com.target.ready.library.system.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.target.ready.library.system.Entity.Book;
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



    @DeleteMapping("deleteBook/{id}")
    public void deleteBook(@PathVariable("id") int id) {
        librarySystemService.deleteBook(id);
    }

}
