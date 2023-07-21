package com.target.ready.library.system.Service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.target.ready.library.system.Entity.Book;
import com.target.ready.library.system.Entity.BookCategory;
import com.target.ready.library.system.Entity.BookDto;
import com.target.ready.library.system.Entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service

public class LibrarySystemService {

    @Autowired
    CategoryService categoryService;
    ObjectMapper objectMapper = new ObjectMapper();
    private final WebClient webclient;
    public LibrarySystemService(WebClient webClient) {
        this.webclient = webClient;
    }
    public List<Book> getAllBooks(){
        List<Book> book_list= webclient.get().uri("http://localhost:8080/library_service_api/v1/allBooks").accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntityList(Book.class)
                .block()
                .getBody();;
        return book_list;
    }

    @Transactional
    public String addBook(BookDto bookDto)  {
        try {

            String b = webclient.post().uri("http://localhost:8080/library_service_api/v1/inventory/books")

                    .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(objectMapper.writeValueAsString(bookDto.getBook()))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            int bookId=Integer.valueOf(b);
            List<String> categoryNames = bookDto.getCategoryNames();
            for (String eachCategoryName : categoryNames) {

                Category category = categoryService.findCategoryBycategoryName(eachCategoryName);
                if (category == null) {
                    Category category1 = new Category();
                    category1.setCategoryName(eachCategoryName);
                    categoryService.addCategory(category1);
                }

                BookCategory bookCategory=new BookCategory();
                bookCategory.setBookId(bookId);
                bookCategory.setCategoryName(eachCategoryName);
                categoryService.addBookCategory(bookCategory);
            }
            return "Book Added Successfully";
        }
        catch (Exception e){
            throw new RuntimeException("Failed to add book and category.", e);
        }
    }


    public Book findByBookId(int bookId) {
        Book book=webclient.get().uri("http://localhost:8080/library_service_api/v1/book/"+bookId).accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Book.class)
                .block();
        return book;
    }
}


