package com.target.ready.library.system.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.target.ready.library.system.dto.BookDto;
import com.target.ready.library.system.dto.BookDtoUpdate;
import com.target.ready.library.system.entity.Book;
import com.target.ready.library.system.exceptions.ResourceAlreadyExistsException;
import com.target.ready.library.system.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository


public  class BookImplementation implements BookRepository {



    private final WebClient webClient;
    @Value("${library.baseUrl}")
    private String libraryBaseUrl;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    public BookImplementation(WebClient webClient) {
        this.webClient = webClient;
    }


    @Override
    public List<Book> findBookByCategoryName(String categoryName, int pageNumber, int pageSize) throws ResourceNotFoundException {
        return webClient.get()
                .uri(libraryBaseUrl + "book/category/" + categoryName + "/" + pageNumber + "/" + pageSize).accept(MediaType.APPLICATION_JSON)
                .exchange()
                .flatMap(response -> {
                    if (response.statusCode().isError() && response.statusCode().value() == 404) {
                        return response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new ResourceNotFoundException(errorBody)));

                    } else {
                        return response.bodyToFlux(Book.class).collectList();
                    }
                })
                .block();
    }

    @Override
    public Mono<Long> countBooksByCategoryName(String categoryName) {
        return webClient
                .get()
                .uri(libraryBaseUrl + "books/category/total_count/" + categoryName)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .flatMap(response -> {
                    if (response.statusCode().isError() && response.statusCode().value() == 404) {
                        return response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new ResourceNotFoundException(errorBody)));

                    } else {
                        return response.bodyToMono(Long.class);
                    }
                });

    }

    @Override
    public List<Book> findByBookName(String bookName) {
        return webClient.get()
                .uri(libraryBaseUrl + "books/" + bookName)
                .accept(MediaType.APPLICATION_JSON)

                .exchange()
                .flatMap(response -> {
                    if (response.statusCode().isError() && response.statusCode().value() == 404) {
                        return response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new ResourceNotFoundException(errorBody)));

                    } else {
                        return response.bodyToFlux(Book.class).collectList();
                    }
                })
                .block();
    }


    @Override
    public List<Book> getAllBooks(int pageNumber, int pageSize) throws ResourceNotFoundException{
        return WebClient.builder()
                .baseUrl(libraryBaseUrl)
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("books_directory/{pageNumber}/{pageSize}")
                        .build(pageNumber, pageSize))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .flatMap(response -> {
                    if (response.statusCode().isError() && response.statusCode().value() == 404) {
                        return response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new ResourceNotFoundException(errorBody)));

                    } else {
                        return response.bodyToFlux(Book.class).collectList();
                    }
                })
                .block();
    }

    @Override
    public Mono<Long> totalBooks() {
        return webClient
                .get()
                .uri(libraryBaseUrl + "books_directory/total_count")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .flatMap(response -> {
                    if (response.statusCode().isError() && response.statusCode().value() == 404) {
                        return response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new ResourceNotFoundException(errorBody)));

                    } else {
                        return response.bodyToMono(Long.class);
                    }
                });

    }

    public Book addBook(BookDto bookDto) throws ResourceAlreadyExistsException, JsonProcessingException {
        return webClient.post()
                .uri(libraryBaseUrl + "inventory/books")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(objectMapper.writeValueAsString(bookDto.getBook()))
                .exchange()
                .flatMap(response -> {
                    if (response.statusCode().isError() && response.statusCode().value() == 409) {
                        return response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new ResourceAlreadyExistsException(errorBody)));

                    } else {
                        return response.bodyToMono(Book.class);
                    }
                })
                .block();

    }


    @Override
    public Book findByBookId(int bookId) throws ResourceNotFoundException{
        return webClient.get()
                .uri(libraryBaseUrl + "book/" + bookId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .flatMap(response -> {
                    if (response.statusCode().isError() && response.statusCode().value() == 404) {
                        return response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new ResourceNotFoundException(errorBody)));

                    } else {
                        return response.bodyToMono(Book.class);
                    }
                })
                .block();
    }


    @Override
    public String deleteBook(int bookId) throws ResourceNotFoundException {
        return webClient.delete()
                .uri(libraryBaseUrl + "books/" + bookId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .flatMap(response -> {
                    if (response.statusCode().isError() && response.statusCode().value() == 404) {
                        return response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new ResourceNotFoundException(errorBody)));

                    } else {
                        return response.bodyToMono(String.class);
                    }
                })
                .block();
    }

    @Override
    public Book updateBookDetails(int bookId, BookDtoUpdate bookDtoUpdate) throws JsonProcessingException, ResourceAlreadyExistsException {

        return webClient.put()
                .uri(libraryBaseUrl + "inventory/book/update/" + bookId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(objectMapper.writeValueAsString(bookDtoUpdate.getBook()))
                .exchange()
                .flatMap(response -> {
                    if (response.statusCode().isError() && response.statusCode().value() == 409) {
                        return response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new ResourceAlreadyExistsException(errorBody)));
                    } else {
                        return response.bodyToMono(Book.class);
                    }
                })
                .block();
    }
}
