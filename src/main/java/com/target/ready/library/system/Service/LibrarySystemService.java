package com.target.ready.library.system.Service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.target.ready.library.system.Entity.Book;
import com.target.ready.library.system.Entity.BookCategory;
import com.target.ready.library.system.Dto.BookDto;
import com.target.ready.library.system.Entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

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

            Book book = webclient.post().uri(libraryBaseUrl + "inventory/books")

                    .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(objectMapper.writeValueAsString(bookDto.getBook()))
                    .retrieve()
                    .bodyToMono(Book.class)
                    .block();
            int bookId = book.getBookId();
            List<String> categoryNames = bookDto.getCategoryNames();
            for (String eachCategoryName : categoryNames) {

                Category category = categoryService.findCategoryBycategoryName(eachCategoryName);
                if (category == null) {
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


        public List<Book> findBookByCategoryName(String categoryName) {
        List<Book> bookList= webclient.get().uri(libraryBaseUrl+"book/category/"+categoryName).accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntityList(Book.class)
                .block()
                .getBody();;
        return bookList;
    }

    public List<Book> findByBookName(String bookName){
       List<Book> books = webclient.get().uri(libraryBaseUrl + "books/" + bookName).accept(MediaType.APPLICATION_JSON)
               .retrieve()
               .toEntityList(Book.class)
               .block()
               .getBody();
       return  books;
    }

    public String deleteBook(int bookId) {
        try {
            String result = webclient.delete().uri(
                            libraryBaseUrl + "book/" + bookId).accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            categoryService.deleteBookCategory(bookId);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete book", e);
        }
    }

    @Transactional
    public String updateBookDetails(int bookId, BookDto bookDto) {
        try {
            Book book = webclient.put().uri(libraryBaseUrl + "inventory/book/update/"+bookId)
                    .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(objectMapper.writeValueAsString(bookDto.getBook()))
                    .retrieve()
                    .bodyToMono(Book.class)
                    .block();
            categoryService.deleteBookCategory(bookId);
            List<String> categoryNames = bookDto.getCategoryNames();
            for (String categoryName : categoryNames) {
                Category category1 = new Category();
                category1.setCategoryName(categoryName);
                categoryService.addCategory(category1);
                BookCategory bookCategory = new BookCategory();
                bookCategory.setBookId(bookId);
                bookCategory.setCategoryName(categoryName);
                categoryService.addBookCategory(bookCategory);
            }
            return "Book updated" ;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update book details", e);
        }
    }


}




