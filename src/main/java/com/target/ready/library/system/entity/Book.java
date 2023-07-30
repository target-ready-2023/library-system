package com.target.ready.library.system.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @JsonProperty("book_id")
    @JsonAlias("bookId")
    private int bookId;

    @JsonProperty("book_name")
    @JsonAlias("bookName")
    private String bookName;

    @JsonProperty("book_description")
    @JsonAlias("bookDescription")
    private String bookDescription;

    @JsonProperty("author_name")
    @JsonAlias("authorName")
    private String authorName;

    @JsonProperty("publication_year")
    @JsonAlias("publicationYear")
    private int publicationYear;
}
