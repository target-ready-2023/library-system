package com.target.ready.library.system.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.target.ready.library.system.entity.Book;
import com.target.ready.library.system.entity.BookCategory;
import com.target.ready.library.system.exceptions.DataAccessException;
import com.target.ready.library.system.exceptions.ResourceAlreadyExistsException;
import com.target.ready.library.system.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public class BookCategoryImplementation implements BookCategoryRepository{

    private final WebClient webClient;

    @Value("${library.baseUrl2}")
    private String libraryBaseUrl2;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    public BookCategoryImplementation(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public BookCategory addBookCategory(BookCategory bookCategory) throws JsonProcessingException,ResourceAlreadyExistsException {

            return webClient.post().uri(libraryBaseUrl2 + "inventory/book/category")

                    .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(objectMapper.writeValueAsString(bookCategory))
                    .exchange()
                    .flatMap(response -> {
                        if (response.statusCode().isError() && response.statusCode().value() == 409 ) {
                            return response.bodyToMono(String.class)
                                    .flatMap(errorBody -> Mono.error(new ResourceAlreadyExistsException(errorBody)));
                        } else {
                            return response.bodyToMono(BookCategory.class);
                        }
                    })
                    .block();

    }

    @Override
    public String deleteBookCategory(int bookId) throws DataAccessException, ResourceNotFoundException {
        String resp = webClient.delete()
                .uri(libraryBaseUrl2 + "inventory/delete/bookCategory/" + bookId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .flatMap(response -> {
                    if (response.statusCode().isError() && response.statusCode().value() == 409) {
                        return response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new ResourceNotFoundException(errorBody)));
                    } else {
                        return response.bodyToMono(String.class);
                    }
                })
                .block();
        return resp;
    }

    @Override
    public List<BookCategory> findAllCategoriesByBookId(int bookId) throws ResourceNotFoundException{
        List<BookCategory> categories= webClient.get().uri(libraryBaseUrl2 + "categories/" + bookId).accept(MediaType.APPLICATION_JSON)
                .exchange()
                .flatMap(response -> {
                    if (response.statusCode().isError() && response.statusCode().value() == 404 ) {
                        return response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new ResourceNotFoundException(errorBody)));
                    } else {
                        return response.bodyToFlux(BookCategory.class).collectList();
                    }
                })
                .block();

        return  categories;
    }
}
