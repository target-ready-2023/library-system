package com.target.ready.library.system.Controller;

import com.target.ready.library.system.Entity.Book;
import com.target.ready.library.system.Service.LibrarySystemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("LibrarySystem/v1")
public class LibraryController {
    private final LibrarySystemService librarySystemService;
    LibraryController(LibrarySystemService librarySystemService){
        this.librarySystemService=librarySystemService;
    }
    @GetMapping("getBooks")
    public List<Book> getAllBooks(){
        return librarySystemService.getAllBooks();
    }

}
