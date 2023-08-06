package com.target.ready.library.system.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.target.ready.library.system.entity.Book;
import com.target.ready.library.system.entity.Category;
import com.target.ready.library.system.exceptions.ClientErrorException;
import com.target.ready.library.system.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public class CategoryImplementation implements CategoryRepository{

    private final WebClient webClient;
    @Value("${library.baseUrl2}")
    private String libraryBaseUrl2;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    public CategoryImplementation(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Category findCategoryBycategoryName(String categoryName) throws ResourceNotFoundException{
        return   webClient.get().uri(libraryBaseUrl2 + "category/" + categoryName)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .flatMap(response -> {
                    if (response.statusCode().isError() && response.statusCode().value() == 404 ) {
                        return response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new ResourceNotFoundException(errorBody)));
                    } else {
                        return response.bodyToMono(Category.class);
                    }
                })
                .block();
    }

    @Override
    public List<Category> findAllCategories(int pageNumber, int pageSize) {
        return WebClient.builder()
                .baseUrl(libraryBaseUrl2)
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("categories/{pageNumber}/{pageSize}")
                        .build(pageNumber, pageSize))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntityList(Category.class)
                .block()
                .getBody();
    }

    @Override
    public void addCategory(Category category) throws ClientErrorException,JsonProcessingException{

            webClient.post().uri(libraryBaseUrl2 + "inventory/category")

                    .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(objectMapper.writeValueAsString(category))
                    .exchange()
                    .flatMap(response -> {
                        if (response.statusCode().isError() && response.statusCode().value() == 500 ) {
                            return response.bodyToMono(String.class)
                                    .flatMap(errorBody -> Mono.error(new ClientErrorException("Client Error: " + errorBody)));
                        } else {
                            return response.bodyToMono(Category.class);
                        }
                    })
                    .block();



    }

}
