package com.target.ready.library.system.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.target.ready.library.system.entity.UserCatalog;
import com.target.ready.library.system.entity.UserProfile;
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
    public Integer deleteBookByUserId(int bookId,int userId) {
        return webClient.delete()
                .uri(libraryBaseUrl3 + "user/books/"+userId+"/"+bookId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Integer.class)
                .block();
    }

    @Override
    public UserCatalog addUserCatalog(UserCatalog userCatalog) throws ResourceAlreadyExistsException,ResourceNotFoundException{
        try {
            return webClient.post()
                    .uri(libraryBaseUrl3 + "user/catalog")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(objectMapper.writeValueAsString(userCatalog))
                    .exchange()
                    .flatMap(response -> {
                        if (response.statusCode().isError() && response.statusCode().value() == 409 ) {
                            return response.bodyToMono(String.class)
                                    .flatMap(errorBody -> Mono.error(new ResourceAlreadyExistsException(errorBody)));

                        } else if (response.statusCode().isError()&&response.statusCode().value()==404) {
                            return response.bodyToMono(String.class)
                                    .flatMap(errorBody -> Mono.error(new ResourceNotFoundException(errorBody)));

                        } else {
                            return response.bodyToMono(UserCatalog.class);
                        }
                    })
                    .block();
        } catch (Exception e) {
            throw new RuntimeException("User with Issuedbook not added", e);
        }
    }

    @Override
    public UserProfile addUser(UserProfile userProfile) throws JsonProcessingException ,ResourceAlreadyExistsException{

            return webClient.post()
                    .uri(libraryBaseUrl3 + "user")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(objectMapper.writeValueAsString(userProfile))
                    .exchange()
                    .flatMap(response -> {
                        if (response.statusCode().isError() && response.statusCode().value() == 409 ) {
                            return response.bodyToMono(String.class)
                                    .flatMap(errorBody -> Mono.error(new ResourceAlreadyExistsException(errorBody)));

                        } else {
                            return response.bodyToMono(UserProfile.class);
                        }
                    })
                    .block();

    }

    @Override
    public UserProfile findByUserId(int userId) {
        return webClient.get().uri(libraryBaseUrl3 + "user/"+userId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(UserProfile.class)
                .block();
    }


    @Override
    public String deleteUser(int userId) {
        return webClient.delete()
                .uri(libraryBaseUrl3 + "delete/user/"+userId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }


    public List<UserProfile> getAllUsers() {
        return   webClient.get().uri(libraryBaseUrl3 + "users")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(UserProfile.class)
                .collectList()
                .block();
    }
}
