package com.target.ready.library.system.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.target.ready.library.system.entity.Category;
import com.target.ready.library.system.entity.Inventory;
import com.target.ready.library.system.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

public class UserImplementation implements UserRepository{
    private final WebClient webClient;
    @Value("${library.baseUrl3}")
    private String libraryBaseUrl3;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    public UserImplementation(WebClient webClient) {
        this.webClient = webClient;
    }


    @Override
    public List<Integer> findBooksByUserId(int userId) {
        return   webClient.get().uri(libraryBaseUrl3 + "user/books/"+userId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Integer.class)
                .collectList()
                .block();
    }
    @Override
    public void deleteBookByUserId(int bookId,int userId) {
        webClient.delete()
                .uri(libraryBaseUrl3 + "user/books/"+userId+"/"+bookId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    @Override
    public User addUser(User user) {
        try {
            return webClient.post()
                    .uri(libraryBaseUrl3 + "inventory")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(objectMapper.writeValueAsString(user))
                    .retrieve()
                    .bodyToMono(User.class)
                    .block();
        } catch (Exception e) {
            throw new RuntimeException("User with Issuedbook not added", e);
        }
    }
}
