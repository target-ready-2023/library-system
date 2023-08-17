package com.target.ready.library.system.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.target.ready.library.system.entity.Book;
import com.target.ready.library.system.entity.Category;
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
    public List<UserCatalog> findBooksByUserId(int userId) throws ResourceNotFoundException{
        return   webClient.get().uri(libraryBaseUrl3 + "user/books/"+userId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .flatMap(response -> {
                    if (response.statusCode().isError() && response.statusCode().value() == 404) {
                        return response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new ResourceNotFoundException(errorBody)));

                    } else {
                        return response.bodyToFlux(UserCatalog.class).collectList();
                    }
                })
                .block();
    }
    @Override
    public Integer deleteBookByUserId(int bookId,int userId) throws ResourceNotFoundException{
        return webClient.delete()
                .uri(libraryBaseUrl3 + "user/books/"+userId+"/"+bookId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .flatMap(response -> {
                    if (response.statusCode().isError() && response.statusCode().value() == 404) {
                        return response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new ResourceNotFoundException(errorBody)));

                    } else {
                        return response.bodyToMono(Integer.class);
                    }
                })
                .block();
    }

    @Override
    public UserCatalog addUserCatalog(UserCatalog userCatalog) throws ResourceAlreadyExistsException,ResourceNotFoundException,JsonProcessingException{

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

    }

    @Override
    public UserProfile addUser(UserProfile userProfile) throws ResourceAlreadyExistsException, JsonProcessingException {

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
    public UserProfile findByUserId(int userId) throws ResourceNotFoundException{
        return webClient.get().uri(libraryBaseUrl3 + "user/"+userId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .flatMap(response -> {
                    if (response.statusCode().isError() && response.statusCode().value() == 404) {
                        return response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new ResourceNotFoundException(errorBody)));

                    } else {
                        return response.bodyToMono(UserProfile.class);
                    }
                })
                .block();
    }


    @Override
    public String deleteUser(int userId) throws ResourceNotFoundException,ResourceAlreadyExistsException{
        return webClient.delete()
                .uri(libraryBaseUrl3 + "delete/user/"+userId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .flatMap(response -> {
                    if (response.statusCode().isError() && response.statusCode().value() == 404) {
                        return response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new ResourceNotFoundException(errorBody)));

                    } else if (response.statusCode().isError() && response.statusCode().value() == 409) {
                        return response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new ResourceAlreadyExistsException(errorBody)));

                    }
                    else {
                        return response.bodyToMono(String.class);
                    }
                })
                .block();}


    public List<UserProfile> getAllUsers(int pageNumber,int pageSize) throws ResourceNotFoundException{

        return WebClient.builder()
                .baseUrl(libraryBaseUrl3)
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("users/{pageNumber}/{pageSize}")
                        .build(pageNumber, pageSize))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .flatMap(response -> {
                    if (response.statusCode().isError() && response.statusCode().value() == 404) {
                        return response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new ResourceNotFoundException(errorBody)));

                    } else {
                        return response.bodyToFlux(UserProfile.class).collectList();
                    }
                })
                .block();
    }

    public List<UserProfile> fetchAllUsers() throws ResourceNotFoundException{
        return   webClient.get().uri(libraryBaseUrl3 + "AllUsers")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .flatMap(response -> {
                    if (response.statusCode().isError() && response.statusCode().value() == 404) {
                        return response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new ResourceNotFoundException(errorBody)));

                    } else {
                        return response.bodyToFlux(UserProfile.class).collectList();
                    }
                })
                .block();
    }

    @Override
    public Mono<Long> totalUsers() {
        return webClient
                .get()
                .uri(libraryBaseUrl3 + "users/total_count")
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
}
