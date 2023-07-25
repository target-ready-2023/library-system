package com.target.ready.library.system.Service;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.target.ready.library.system.Entity.Book;
import com.target.ready.library.system.Entity.BookCategory;
import com.target.ready.library.system.Entity.BookDto;
import com.target.ready.library.system.Entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;


import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;


@Service

public class LibrarySystemService {
    @Value("${library.baseUrl}")
    private String libraryBaseUrl;
    @Autowired
    CategoryService categoryService;

    @Autowired

    ObjectMapper objectMapper;


    private final WebClient webclient;

    public LibrarySystemService(WebClient webClient) {
        this.webclient = webClient;
    }

    public List<Book> getAllBooks(int pageNumber, int pageSize) {
        List<Book> bookList = WebClient.builder()
                .baseUrl(libraryBaseUrl)
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/books_directory/{pageNumber}/{pageSize}")
                        .build(pageNumber, pageSize))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntityList(Book.class)
                .block()
                .getBody();
        return bookList;
    }

    @Transactional
    public String addBook(BookDto bookDto) {

            try {

                String bookAddedResult = webclient.post().uri(libraryBaseUrl + "inventory/books")

                        .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(objectMapper.writeValueAsString(bookDto.getBook()))
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();
                int bookId = Integer.valueOf(bookAddedResult);
                List<String> categoryNames = bookDto.getCategoryNames();
                for (String eachCategoryName : categoryNames) {

                    Category category = categoryService.findCategoryBycategoryName(eachCategoryName);
                    if (category != null) {
                        Category category1 = new Category();
                        category1.setCategoryName(eachCategoryName);
                        categoryService.addCategory(category1);
                    }

                    BookCategory bookCategory = new BookCategory();
                    bookCategory.setBookId(bookId);
                    bookCategory.setCategoryName(eachCategoryName);
                    categoryService.addBookCategory(bookCategory);
                }
                return "Book Added Successfully";

            } catch (Exception e) {
                throw new RuntimeException("Failed to add book and category.", e);
            }


    }


    public Book findByBookId(int bookId) {
        Book book = webclient.get().uri(libraryBaseUrl + "book/" + bookId).accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Book.class)
                .block();
        return book;
    }

    //    public List<Book> findBookByCategoryName(String categoryName) {
//        List<Book> bookList= webclient.get().uri("http://localhost:8080/library/v1/book/category/"+categoryName).accept(MediaType.APPLICATION_JSON)
//                .retrieve()
//                .toEntityList(Book.class)
//                .block()
//                .getBody();;
//        return bookList;
//    }
    public String deleteBook(int bookId) {
        try {
            String result = webclient.delete().uri(
                            libraryBaseUrl + "book/" + bookId).accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete book", e);
        }
    }

    public Book updateBookDetails(int bookId, Book book) {
        try {
            WebClient.RequestBodySpec request = webclient.put()
                    .uri(libraryBaseUrl + "inventory/book_update/" + bookId)
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
