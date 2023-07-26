package com.target.ready.library.system.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.target.ready.library.system.Entity.BookCategory;
import com.target.ready.library.system.Entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

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
            Category category1 = webclient.post().uri(libraryBaseUrl2 + "inventory/category")

                    .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(objectMapper.writeValueAsString(category))
                    .retrieve()
                    .bodyToMono(Category.class)
                    .block();

            return "Category Added Successfully";

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

    public List<Category> findAllCategories() {
        try {
            String response = webclient.get()
                    .uri(libraryBaseUrl2 + "/categories")
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return objectMapper.readValue(response, new TypeReference<List<Category>>() {});
        } catch (Exception e) {
            // Handle exceptions or log the error
            e.printStackTrace();
            return null;
        }
    }


    public  String addBookCategory(BookCategory bookCategory) {
        try {
            BookCategory bookCategory1 = webclient.post().uri(libraryBaseUrl2 + "inventory/book/category")

                    .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(objectMapper.writeValueAsString(bookCategory))
                    .retrieve()
                    .bodyToMono(BookCategory.class)
                    .block();
            return "Book with all its categories added";

        } catch (Exception e) {
            throw new RuntimeException("Failed to add", e);
        }
    }

    public String deleteBookCategory(int bookId) {
        try {
            BookCategory bookCategory = webclient.get().uri(libraryBaseUrl2 + "category/book/" + bookId)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(BookCategory.class)
                    .block();
            if(bookCategory!=null) {
                String result = webclient.delete().uri(libraryBaseUrl2 + "inventory/book/category/" + bookCategory.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();

                return result;
            }
            return "Book Category is empty";
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete", e);
        }
    }
}