package com.target.ready.library.system.Service;


import com.target.ready.library.system.Entity.Book;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class LibrarySystemService {
    private final WebClient webclient;
    public LibrarySystemService(WebClient webClient) {
        this.webclient = webClient;
    }
    public List<Book> getAllBooks(){
        List<Book> book_list= webclient.get().uri("http://localhost:8080/library_service_api/v1/getAllBooks").accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntityList(Book.class)
                .block()
                .getBody();;
        return book_list;
    }

    public void deleteBook(int id){
        webclient.delete().uri(
                "http://localhost:8080/library_service_api/v1/deleteBook/"+id).accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
