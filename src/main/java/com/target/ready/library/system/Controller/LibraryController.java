package com.target.ready.library.system.Controller;

import com.target.ready.library.system.Entity.Book;
import com.target.ready.library.system.Service.LibrarySystemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.List;

@RestController
@RequestMapping("LibrarySystem/v1")
public class LibraryController {
    private LibrarySystemService librarySystemService = null;
    LibraryController(LibrarySystemService librarySystemService){
        this.librarySystemService=librarySystemService;
    }
    @GetMapping("getBooks")
    public List<Book> getAllBooks(){
        return librarySystemService.getAllBooks();
    }
    @DeleteMapping("deleteBook/{id}")
    public void deleteBook(@PathVariable("id") int id) {
        librarySystemService.deleteBook(id);
    }

}
