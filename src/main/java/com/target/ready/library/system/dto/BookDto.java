package com.target.ready.library.system.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.target.ready.library.system.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

    private Book book;
    @JsonProperty("category_names")
    @JsonAlias("categoryNames")
    private List<String> categoryNames;
    @JsonProperty("no_of_copies")
    @JsonAlias("noOfCopies")
    private int noOfCopies;
}
