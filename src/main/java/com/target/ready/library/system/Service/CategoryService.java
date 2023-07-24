package com.target.ready.library.system.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.target.ready.library.system.Entity.BookCategory;
import com.target.ready.library.system.Entity.Category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import org.springframework.web.reactive.function.client.WebClient;

@Service
public class CategoryService {
    @Value("${library.baseUrl2}")
    private String libraryBaseUrl2;
    @Autowired
    ObjectMapper objectMapper;
    private final WebClient webclient;

    public CategoryService(WebClient webClient) {
        this.webclient = webClient;
    }

    public String addCategory(Category category) {
        try {
            String result = webclient.post().uri(libraryBaseUrl2 + "inventory/category")

                    .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(objectMapper.writeValueAsString(category))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            return result;

        } catch (Exception e) {
            throw new RuntimeException("Failed to add category", e);
        }
    }

    public Category findCategoryBycategoryName(String categoryName) {
        Category result = webclient.get().uri(libraryBaseUrl2 + "category/" + categoryName)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Category.class)
                .block();
        return result;
    }


    public String addBookCategory(BookCategory bookCategory) {
        try {
            String result = webclient.post().uri(libraryBaseUrl2 + "inventory/book/category")

                    .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(objectMapper.writeValueAsString(bookCategory))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            return result;

        } catch (Exception e) {
            throw new RuntimeException("Failed to add", e);
        }
    }
}