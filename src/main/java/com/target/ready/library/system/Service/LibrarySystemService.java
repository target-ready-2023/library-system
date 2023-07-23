package com.target.ready.library.system.Service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.target.ready.library.system.Entity.Book;
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
    @Autowired
    ObjectMapper objectMapper;
    private final WebClient webclient;
    public LibrarySystemService(WebClient webClient) {
        this.webclient = webClient;
    }
    public List<Book> getAllBooks(){
        List<Book> book_list= webclient.get().uri("http://localhost:8080/library/v1/books").accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntityList(Book.class)
                .block()
                .getBody();;
        return book_list;
    }

    @Transactional
    public String addBook(Book book,String categoryName)  {
        try {

            String result = webclient.post().uri("http://localhost:8080/library/v1/inventory/books")

                    .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(objectMapper.writeValueAsString(book))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();


            Category category=categoryService.findCategoryBycategoryName(categoryName);
            if(category==null){
                Category category1=new Category();
                category1.setCategoryName(categoryName);
                categoryService.addCategory(category1);
            }
            return result;
        }
        catch (Exception e){
            throw new RuntimeException("Failed to add book and category.", e);
        }
        }

    public Book findByBookId(int bookId) {
        Book book=webclient.get().uri("http://localhost:8080/library/v1/book/"+bookId).accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Book.class)
                .block();
        return book;
    }

    public List<Book> findBookByCategoryName(String categoryName) {
        List<Book> bookList= webclient.get().uri("http://localhost:8080/library/v1/book/category/"+categoryName).accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntityList(Book.class)
                .block()
                .getBody();;
        return bookList;
    }
    public String deleteBook(int bookId){
        try {
            String result = webclient.delete().uri(
                            "http://localhost:8080/library/v1/book/" + bookId).accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            return result;
        }
        catch (Exception e){
            throw new RuntimeException("Failed to delete book", e);
        }
    }

    public Book updateBookDetails(int bookId, Book book) {
        try {
            WebClient.RequestBodySpec request = webclient.put()
                    .uri("http://localhost:8080/library/v1/inventory/book_update/" + bookId)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON);
            String bookJson = objectMapper.writeValueAsString(book);
            WebClient.ResponseSpec response = request.bodyValue(bookJson).retrieve();
            Book updatedBook = response.bodyToMono(Book.class).block();
            return updatedBook;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update book details", e);
        }
    }

}
