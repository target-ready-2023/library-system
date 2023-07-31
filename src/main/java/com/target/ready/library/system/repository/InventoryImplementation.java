package com.target.ready.library.system.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.target.ready.library.system.entity.Book;
import com.target.ready.library.system.entity.Category;
import com.target.ready.library.system.entity.Inventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;

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
    public Inventory findByBookId(int bookId) {
        return   webClient.get().uri(libraryBaseUrl + "inventory/book/"+ bookId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Inventory.class)
                .block();
    }

    @Override
    public Inventory addInventory(Inventory inventory) {
        try {
            return webClient.post()
                    .uri(libraryBaseUrl + "inventory")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(objectMapper.writeValueAsString(inventory))
                    .retrieve()
                    .bodyToMono(Inventory.class)
                    .block();
        } catch (Exception e) {
            throw new RuntimeException("Failed to add book and category.", e);
        }
    }

}
