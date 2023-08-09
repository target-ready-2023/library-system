package com.target.ready.library.system.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.target.ready.library.system.entity.Book;
import com.target.ready.library.system.entity.Category;
import com.target.ready.library.system.entity.Inventory;
import com.target.ready.library.system.exceptions.ResourceAlreadyExistsException;
import com.target.ready.library.system.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Repository
public class InventoryImplementation implements InventoryRepository{
    private final WebClient webClient;
    @Value("${library.baseUrl}")
    private String libraryBaseUrl;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    public InventoryImplementation(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Inventory findByBookId(int bookId) throws ResourceNotFoundException{
        return   webClient.get().uri(libraryBaseUrl + "inventory/book/"+ bookId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .flatMap(response -> {
                    if (response.statusCode().isError() && response.statusCode().value() == 404 ) {
                        return response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new ResourceNotFoundException(errorBody)));

                    } else {
                        return response.bodyToMono(Inventory.class);
                    }
                })
                .block();
    }

    @Override
    public Inventory addInventory(Inventory inventory) throws ResourceAlreadyExistsException
    {

        try {
            return webClient.post()
                    .uri(libraryBaseUrl + "inventory")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(objectMapper.writeValueAsString(inventory))
                    .exchange()
                    .flatMap(response -> {
                        if (response.statusCode().isError() && response.statusCode().value() == 409 ) {
                            return response.bodyToMono(String.class)
                                    .flatMap(errorBody -> Mono.error(new ResourceAlreadyExistsException(errorBody)));

                        } else {
                            return response.bodyToMono(Inventory.class);
                        }
                    })
                    .block();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

}
