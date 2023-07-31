package com.target.ready.library.system.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.target.ready.library.system.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;

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
    public Category findCategoryBycategoryName(String categoryName) {
        return   webClient.get().uri(libraryBaseUrl2 + "category/" + categoryName)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Category.class)
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
    public void addCategory(Category category) {
        try {
            webClient.post().uri(libraryBaseUrl2 + "inventory/category")

                    .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(objectMapper.writeValueAsString(category))
                    .retrieve()
                    .bodyToMono(Category.class)
                    .block();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
